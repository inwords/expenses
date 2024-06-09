import { MigrationInterface, QueryRunner } from "typeorm";

export class AddPinCodeToEvent1717938197068 implements MigrationInterface {
    name = 'AddPinCodeToEvent1717938197068'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            ALTER TABLE "event"
            ADD "pin_code" character varying NOT NULL
        `);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            ALTER TABLE "event" DROP COLUMN "pin_code"
        `);
    }

}
