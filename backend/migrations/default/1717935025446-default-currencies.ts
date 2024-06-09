import {MigrationInterface, QueryRunner} from 'typeorm';

export class DefaultCurrencies1717935025446 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
      INSERT INTO currency (id, type) VALUES (1, 'USD'), (2, 'EUR'), (3, 'RUB');
    `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
      DELETE FROM currency WHERE id IN (1, 2, 3);
    `);
  }
}
