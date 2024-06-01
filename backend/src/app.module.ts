import {Module} from '@nestjs/common';
import {AppController} from './app.controller';
import {AppService} from './app.service';
import {TypeOrmModule} from '@nestjs/typeorm';
import {config} from './config';
import {ConfigModule, ConfigService} from '@nestjs/config';
import {Currency} from './currency/currency.entity';
import {Event} from './event/event.entity';

@Module({
  imports: [
    ConfigModule.forRoot({envFilePath: '.env', load: [config]}),
    TypeOrmModule.forRootAsync({
      useFactory: async (configService: ConfigService) => ({
        type: 'postgres',
        host: configService.get('POSTGRES_HOST'),
        port: configService.get('POSTGRES_PORT'),
        username: configService.get('POSTGRES_USER_NAME'),
        password: configService.get('POSTGRES_PASSWORD'),
        database: configService.get('POSTGRES_DATABASE'),
        entities: [Currency, Event],
        migrations: ['migrations/default/**/*.{ts,js}'],
      }),
      inject: [ConfigService],
      imports: [ConfigModule],
    }),
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
