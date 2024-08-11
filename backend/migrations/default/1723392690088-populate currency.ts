import { MigrationInterface, QueryRunner } from "typeorm";

export class PopulateCurrency1723392690088 implements MigrationInterface {
    name = 'PopulateCurrency1723392690088'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            INSERT INTO currency (code) VALUES ('EUR');
            INSERT INTO currency (code) VALUES ('USD');
            INSERT INTO currency (code) VALUES ('RUB');
            INSERT INTO currency (code) VALUES ('JPY');
            INSERT INTO currency (code) VALUES ('TRY');
        `);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            DELETE FROM currency WHERE code IN ('EUR', 'USD', 'RUB', 'JPY', 'TRY');
        `);
    }

}
