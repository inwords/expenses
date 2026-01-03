import { MigrationInterface, QueryRunner } from "typeorm";

export class Init1767464629981 implements MigrationInterface {
    name = 'Init1767464629981'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            CREATE TABLE "event_share_token" (
                "token" character varying(64) NOT NULL,
                "event_id" character varying NOT NULL,
                "expires_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                CONSTRAINT "pk__event_share_token__token" PRIMARY KEY ("token")
            )
        `);
        await queryRunner.query(`
            CREATE INDEX "idx__event_share_token__event_id" ON "event_share_token" ("event_id")
        `);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            DROP INDEX "public"."idx__event_share_token__event_id"
        `);
        await queryRunner.query(`
            DROP TABLE "event_share_token"
        `);
    }

}
