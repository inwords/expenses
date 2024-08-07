import { MigrationInterface, QueryRunner } from "typeorm";

export class ChangeExpnseRelations1719070316037 implements MigrationInterface {
    name = 'ChangeExpnseRelations1719070316037'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            ALTER TABLE "expense" DROP CONSTRAINT "FK_9de8d585bd7169e22bd6d085c78"
        `);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`
            ALTER TABLE "expense"
            ADD CONSTRAINT "FK_9de8d585bd7169e22bd6d085c78" FOREIGN KEY ("user_who_paid_id") REFERENCES "currency"("id") ON DELETE NO ACTION ON UPDATE NO ACTION
        `);
    }

}
