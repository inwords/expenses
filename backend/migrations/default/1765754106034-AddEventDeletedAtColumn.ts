import { MigrationInterface, QueryRunner } from "typeorm";

export class AddEventDeletedAtColumn1765754106034 implements MigrationInterface {
    name = 'AddEventDeletedAtColumn1765754106034'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "event" ADD "deleted_at" TIMESTAMP WITH TIME ZONE`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "event" DROP COLUMN "deleted_at"`);
    }

}
