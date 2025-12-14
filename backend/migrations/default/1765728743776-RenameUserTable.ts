import {MigrationInterface, QueryRunner} from 'typeorm';

export class RenameUserTable1765728743776 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    // Переименовываем таблицу user в user_info
    await queryRunner.query(`ALTER TABLE "user" RENAME TO "user_info"`);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    // Возвращаем таблицу user из user_info
    await queryRunner.query(`ALTER TABLE "user_info" RENAME TO "user"`);
  }
}
