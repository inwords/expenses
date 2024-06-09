import {MigrationInterface, QueryRunner} from 'typeorm';

export class AddUsersToEvent1717930109061 implements MigrationInterface {
  name = 'AddUsersToEvent1717930109061';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "event"
            ADD "users" jsonb NOT NULL
        `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "event" DROP COLUMN "users"
        `);
  }
}
