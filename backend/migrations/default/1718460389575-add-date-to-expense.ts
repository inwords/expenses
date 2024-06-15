import {MigrationInterface, QueryRunner} from 'typeorm';

export class AddDateToExpense1718460389575 implements MigrationInterface {
  name = 'AddDateToExpense1718460389575';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "expense"
            ADD "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
        `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "expense" DROP COLUMN "created_at"
        `);
  }
}
