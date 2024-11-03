import {Module} from '@nestjs/common';
import {TypeOrmModule} from '@nestjs/typeorm';
import {config} from './config';
import {ConfigModule, ConfigService} from '@nestjs/config';
import {join} from 'path';
import {SnakeNamingStrategy} from 'typeorm-naming-strategies';
import {HttpModule} from '#api/http/http.module';

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
    HttpModule,
  ],
})
export class AppModule {}
