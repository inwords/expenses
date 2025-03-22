import './otel';
import {NestFactory} from '@nestjs/core';
import {AppModule} from './app.module';
import {ValidationPipe} from '@nestjs/common';
import {DocumentBuilder, SwaggerModule} from '@nestjs/swagger';
import {MicroserviceOptions, Transport} from '@nestjs/microservices';
import {join} from 'path';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      forbidNonWhitelisted: true,
      transform: true,
      transformOptions: {
        enableImplicitConversion: true,
      },
    }),
  );

  const config = new DocumentBuilder().setTitle('Expenses Swagger').setVersion('0.0.1').build();
  const document = SwaggerModule.createDocument(app, config);

  SwaggerModule.setup('swagger/api', app, document);
  app.enableCors({origin: '*'});

  app.connectMicroservice<MicroserviceOptions>({
    transport: Transport.GRPC,
    options: {
      package: 'user', // This should match the gRPC service package name
      protoPath: join(__dirname, '../expenses.proto'), // Path to your .proto file
    },
  });

  await app.startAllMicroservices();
  await app.listen(3001);
}

bootstrap();
