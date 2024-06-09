import {MigrationInterface, QueryRunner} from 'typeorm';

export class AddEventAndCurrencyEntity1716788311661 implements MigrationInterface {
  name = 'AddEventAndCurrencyEntity1716788311661';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            CREATE TYPE "currency_type_enum" AS ENUM('EUR', 'USD', 'RUB')
        `);
    await queryRunner.query(`
            CREATE TABLE "currency" (
                "id" SERIAL NOT NULL,
                "type" "currency_type_enum" NOT NULL,
                CONSTRAINT "PK_3cda65c731a6264f0e444cc9b91" PRIMARY KEY ("id")
            )
        `);
    await queryRunner.query(`
            CREATE TABLE "event" (
                "id" SERIAL NOT NULL,
                "name" character varying NOT NULL,
                "owner" character varying NOT NULL,
                "currencyId" integer,
                CONSTRAINT "PK_30c2f3bbaf6d34a55f8ae6e4614" PRIMARY KEY ("id")
            )
        `);
    await queryRunner.query(`
            ALTER TABLE "event"
            ADD CONSTRAINT "FK_9203643d4fa9c86cc1d9e295a78" FOREIGN KEY ("currencyId") REFERENCES "currency"("id") ON DELETE NO ACTION ON UPDATE NO ACTION
        `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "event" DROP CONSTRAINT "FK_9203643d4fa9c86cc1d9e295a78"
        `);
    await queryRunner.query(`
            DROP TABLE "event"
        `);
    await queryRunner.query(`
            DROP TABLE "currency"
        `);
    await queryRunner.query(`
            DROP TYPE "currency_type_enum"
        `);
  }
}
