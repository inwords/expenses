import {MigrationInterface, QueryRunner} from 'typeorm';

export class AddExpenseType1724433592185 implements MigrationInterface {
  name = 'AddExpenseType1724433592185';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            CREATE TYPE "public"."expense_expense_type_enum" AS ENUM('expense', 'refund')
        `);
    await queryRunner.query(`
            ALTER TABLE "expense"
            ADD "expense_type" "public"."expense_expense_type_enum" NOT NULL DEFAULT 'expense'
        `);
    await queryRunner.query(`
            ALTER TABLE "expense"
            ALTER COLUMN "expense_type" DROP DEFAULT
        `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "expense" DROP COLUMN "expense_type"
        `);
    await queryRunner.query(`
            DROP TYPE "public"."expense_expense_type_enum"
        `);
  }
}
