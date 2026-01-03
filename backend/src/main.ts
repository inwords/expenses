import './otel';
import {NestFactory} from '@nestjs/core';
import {AppModule} from './app.module';
import {ValidationPipe} from '@nestjs/common';
import {DocumentBuilder, SwaggerModule} from '@nestjs/swagger';
import {MicroserviceOptions, Transport} from '@nestjs/microservices';
import {join} from 'path';
import {BusinessErrorFilter} from './api/http/filters/business-error.filter';

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

  app.useGlobalFilters(new BusinessErrorFilter());

  const config = new DocumentBuilder()
    .setTitle('Expenses Swagger')
    .setVersion('0.0.1')
    .addApiKey(
      {
        type: 'apiKey',
        name: 'x-devtools-secret',
        in: 'header',
        description: 'Devtools secret for accessing devtools endpoints',
      },
      'devtools-secret',
    )
    .build();
  const document = SwaggerModule.createDocument(app, config);

  SwaggerModule.setup('swagger/api', app, document);
  app.enableCors({origin: '*'});

  app.connectMicroservice<MicroserviceOptions>({
    transport: Transport.GRPC,
    options: {
      package: 'user', // This should match the gRPC service package name
      protoPath: join(__dirname, '../expenses.proto'), // Path to your .proto file
      url: '0.0.0.0:5000',
    },
  });

  await app.startAllMicroservices();
  await app.listen(3001);
}

bootstrap();
