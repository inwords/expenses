import {MigrationInterface, QueryRunner} from 'typeorm';

export class ChangeOwnerToOwnerId1717931820513 implements MigrationInterface {
  name = 'ChangeOwnerToOwnerId1717931820513';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "event"
                RENAME COLUMN "owner" TO "owner_id"
        `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "event"
                RENAME COLUMN "owner_id" TO "owner"
        `);
  }
}
