import { MigrationInterface, QueryRunner } from "typeorm";

export class AddDeletedEvent1750591686450 implements MigrationInterface {
    name = 'AddDeletedEvent1750591686450'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            CREATE TABLE "deleted_event" (
                "event_id" character varying NOT NULL,
                "deleted_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                CONSTRAINT "pk__deleted_event__event_id" PRIMARY KEY ("event_id")
            )
        `);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            DROP TABLE "deleted_event"
        `);
    }
}
