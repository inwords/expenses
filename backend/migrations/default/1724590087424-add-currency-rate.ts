import {MigrationInterface, QueryRunner} from 'typeorm';

export class AddCurrencyRate1724590087424 implements MigrationInterface {
  name = 'AddCurrencyRate1724590087424';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            CREATE TABLE "currency_rate" (
                "date" date NOT NULL,
                "rate" jsonb NOT NULL,
                CONSTRAINT "pk__currency__rate__date" PRIMARY KEY ("date")
            )
        `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            DROP TABLE "currency_rate"
        `);
  }
}
