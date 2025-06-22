import { MigrationInterface, QueryRunner } from "typeorm";

export class Init1750591686449 implements MigrationInterface {
    name = 'Init1750591686449'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            CREATE TABLE "currency" (
                "id" character varying NOT NULL,
                "code" character varying NOT NULL,
                "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                CONSTRAINT "pk__currency__id" PRIMARY KEY ("id")
            )
        `);
        await queryRunner.query(`
            CREATE TABLE "event" (
                "id" character varying NOT NULL,
                "name" character varying NOT NULL,
                "currency_id" character varying NOT NULL,
                "pin_code" character varying NOT NULL,
                "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                CONSTRAINT "pk__event__id" PRIMARY KEY ("id")
            )
        `);
        await queryRunner.query(`
            CREATE TABLE "currency_rate" (
                "date" date NOT NULL,
                "rate" jsonb NOT NULL,
                "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                CONSTRAINT "pk__currency_rate__date" PRIMARY KEY ("date")
            )
        `);
        await queryRunner.query(`
            CREATE TABLE "expense" (
                "id" character varying NOT NULL,
                "description" character varying NOT NULL,
                "user_who_paid_id" character varying NOT NULL,
                "currency_id" character varying NOT NULL,
                "event_id" character varying NOT NULL,
                "expense_type" character varying NOT NULL,
                "split_information" jsonb NOT NULL,
                "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                CONSTRAINT "pk__expense__id" PRIMARY KEY ("id")
            )
        `);
        await queryRunner.query(`
            CREATE TABLE "user" (
                "id" character varying NOT NULL,
                "name" character varying NOT NULL,
                "event_id" character varying NOT NULL,
                "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                CONSTRAINT "pk__user__id" PRIMARY KEY ("id")
            )
        `);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            DROP TABLE "user"
        `);
        await queryRunner.query(`
            DROP TABLE "expense"
        `);
        await queryRunner.query(`
            DROP TABLE "currency_rate"
        `);
        await queryRunner.query(`
            DROP TABLE "event"
        `);
        await queryRunner.query(`
            DROP TABLE "currency"
        `);
    }

}
