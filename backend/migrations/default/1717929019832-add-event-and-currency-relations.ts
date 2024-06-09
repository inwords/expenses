import {MigrationInterface, QueryRunner} from 'typeorm';

export class AddEventAndCurrencyRelations1717929019832 implements MigrationInterface {
  name = 'AddEventAndCurrencyRelations1717929019832';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "event" DROP CONSTRAINT "FK_9203643d4fa9c86cc1d9e295a78"
        `);
    await queryRunner.query(`
            ALTER TABLE "event"
                RENAME COLUMN "currencyId" TO "currency_id"
        `);
    await queryRunner.query(`
            ALTER TABLE "event"
            ALTER COLUMN "currency_id"
            SET NOT NULL
        `);
    await queryRunner.query(`
            ALTER TABLE "event"
            ADD CONSTRAINT "FK_535f5fdc7c496da638ebc75eeb9" FOREIGN KEY ("currency_id") REFERENCES "currency"("id") ON DELETE NO ACTION ON UPDATE NO ACTION
        `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "event" DROP CONSTRAINT "FK_535f5fdc7c496da638ebc75eeb9"
        `);
    await queryRunner.query(`
            ALTER TABLE "event"
            ALTER COLUMN "currency_id" DROP NOT NULL
        `);
    await queryRunner.query(`
            ALTER TABLE "event"
                RENAME COLUMN "currency_id" TO "currencyId"
        `);
    await queryRunner.query(`
            ALTER TABLE "event"
            ADD CONSTRAINT "FK_9203643d4fa9c86cc1d9e295a78" FOREIGN KEY ("currencyId") REFERENCES "currency"("id") ON DELETE NO ACTION ON UPDATE NO ACTION
        `);
  }
}
