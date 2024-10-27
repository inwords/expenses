import {Module} from '@nestjs/common';
import {TypeOrmModule} from '@nestjs/typeorm';
import {config} from './config';
import {ConfigModule, ConfigService} from '@nestjs/config';
import {join} from 'path';
import {SnakeNamingStrategy} from 'typeorm-naming-strategies';
import {EventModule} from './event/event.module';
import {CurrencyModule} from './currency/currency.module';
import {HttpModule} from '@nestjs/axios';
import {CronModule} from './api/cron/cron.module';

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
        entities: [join(__dirname, `/**/*.entity.{ts,js}`)],
        migrations: [join(__dirname, `migrations/default/**/*.{ts,js}`)],
        namingStrategy: new SnakeNamingStrategy(),
      }),
      inject: [ConfigService],
      imports: [ConfigModule],
    }),
    EventModule,
    CurrencyModule,
    HttpModule,
    CronModule,
  ],
})
export class AppModule {}
