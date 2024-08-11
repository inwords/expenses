import { MigrationInterface, QueryRunner } from "typeorm";

export class Initialize1723392437959 implements MigrationInterface {
    name = 'Initialize1723392437959'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            CREATE TYPE "public"."currency_code_enum" AS ENUM('EUR', 'USD', 'RUB', 'JPY', 'TRY')
        `);
        await queryRunner.query(`
            CREATE TABLE "currency" (
                "id" SERIAL NOT NULL,
                "code" "public"."currency_code_enum" NOT NULL,
                CONSTRAINT "PK_3cda65c731a6264f0e444cc9b91" PRIMARY KEY ("id")
            )
        `);
        await queryRunner.query(`
            CREATE TABLE "event" (
                "id" SERIAL NOT NULL,
                "name" character varying NOT NULL,
                "currency_id" integer NOT NULL,
                "pin_code" character varying NOT NULL,
                CONSTRAINT "PK_30c2f3bbaf6d34a55f8ae6e4614" PRIMARY KEY ("id")
            )
        `);
        await queryRunner.query(`
            CREATE TABLE "user" (
                "id" SERIAL NOT NULL,
                "name" character varying NOT NULL,
                "event_id" integer NOT NULL,
                CONSTRAINT "PK_cace4a159ff9f2512dd42373760" PRIMARY KEY ("id")
            )
        `);
        await queryRunner.query(`
            CREATE TABLE "expense" (
                "id" SERIAL NOT NULL,
                "description" character varying NOT NULL,
                "user_who_paid_id" integer NOT NULL,
                "currency_id" integer NOT NULL,
                "event_id" integer NOT NULL,
                "split_information" jsonb NOT NULL,
                "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                CONSTRAINT "PK_edd925b450e13ea36197c9590fc" PRIMARY KEY ("id")
            )
        `);
        await queryRunner.query(`
            ALTER TABLE "event"
            ADD CONSTRAINT "FK_535f5fdc7c496da638ebc75eeb9" FOREIGN KEY ("currency_id") REFERENCES "currency"("id") ON DELETE NO ACTION ON UPDATE NO ACTION
        `);
        await queryRunner.query(`
            ALTER TABLE "user"
            ADD CONSTRAINT "FK_4245a6b002b13f12e426d9db3ff" FOREIGN KEY ("event_id") REFERENCES "event"("id") ON DELETE NO ACTION ON UPDATE NO ACTION
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
            ALTER TABLE "user" DROP CONSTRAINT "FK_4245a6b002b13f12e426d9db3ff"
        `);
        await queryRunner.query(`
            ALTER TABLE "event" DROP CONSTRAINT "FK_535f5fdc7c496da638ebc75eeb9"
        `);
        await queryRunner.query(`
            DROP TABLE "expense"
        `);
        await queryRunner.query(`
            DROP TABLE "user"
        `);
        await queryRunner.query(`
            DROP TABLE "event"
        `);
        await queryRunner.query(`
            DROP TABLE "currency"
        `);
        await queryRunner.query(`
            DROP TYPE "public"."currency_code_enum"
        `);
    }

}
