import {MigrationInterface, QueryRunner} from 'typeorm';

export class AddExpense1717945563628 implements MigrationInterface {
  name = 'AddExpense1717945563628';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            CREATE TABLE "expense" (
                "id" SERIAL NOT NULL,
                "name" character varying NOT NULL,
                "amount" numeric(10, 2) NOT NULL,
                "user_who_paid" character varying NOT NULL,
                "currency_id" integer NOT NULL,
                "event_id" integer NOT NULL,
                "split_information" jsonb NOT NULL,
                CONSTRAINT "PK_edd925b450e13ea36197c9590fc" PRIMARY KEY ("id")
            )
        `);
    await queryRunner.query(`
            ALTER TABLE "expense"
            ADD CONSTRAINT "FK_0faf8e64b8acbe65bbd92366578" FOREIGN KEY ("currency_id") REFERENCES "currency"("id") ON DELETE NO ACTION ON UPDATE NO ACTION
        `);
    await queryRunner.query(`
            ALTER TABLE "expense"
            ADD CONSTRAINT "FK_e3fd6476ea867ea03a8f57ef5c5" FOREIGN KEY ("event_id") REFERENCES "event"("id") ON DELETE NO ACTION ON UPDATE NO ACTION
        `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "expense" DROP CONSTRAINT "FK_e3fd6476ea867ea03a8f57ef5c5"
        `);
    await queryRunner.query(`
            ALTER TABLE "expense" DROP CONSTRAINT "FK_0faf8e64b8acbe65bbd92366578"
        `);
    await queryRunner.query(`
            DROP TABLE "expense"
        `);
  }
}
