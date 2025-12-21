import {MigrationInterface, QueryRunner} from 'typeorm';

export class Init1766314597487 implements MigrationInterface {
  name = 'Init1766314597487';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "event"
            ADD "deleted_at" TIMESTAMP WITH TIME ZONE
        `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "event" DROP COLUMN "deleted_at"
        `);
  }
}
