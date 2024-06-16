import {MigrationInterface, QueryRunner} from 'typeorm';

export class RemoveOwnerId1718448864288 implements MigrationInterface {
  name = 'RemoveOwnerId1718448864288';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            CREATE TABLE "user" (
                "id" SERIAL NOT NULL,
                "name" character varying NOT NULL,
                "event_id" integer NOT NULL,
                CONSTRAINT "PK_cace4a159ff9f2512dd42373760" PRIMARY KEY ("id")
            )
        `);
    await queryRunner.query(`
            ALTER TABLE "event" DROP COLUMN "owner_id"
        `);
    await queryRunner.query(`
            ALTER TABLE "event" DROP COLUMN "users"
        `);
    await queryRunner.query(`
            ALTER TABLE "expense" DROP COLUMN "name"
        `);
    await queryRunner.query(`
            ALTER TABLE "expense" DROP COLUMN "amount"
        `);
    await queryRunner.query(`
            ALTER TABLE "expense" DROP COLUMN "user_who_paid"
        `);
    await queryRunner.query(`
            ALTER TABLE "expense"
            ADD "description" character varying NOT NULL
        `);
    await queryRunner.query(`
            ALTER TABLE "expense"
            ADD "user_who_paid_id" integer NOT NULL
        `);
    await queryRunner.query(`
            ALTER TABLE "user"
            ADD CONSTRAINT "FK_4245a6b002b13f12e426d9db3ff" FOREIGN KEY ("event_id") REFERENCES "event"("id") ON DELETE NO ACTION ON UPDATE NO ACTION
        `);
    await queryRunner.query(`
            ALTER TABLE "expense"
            ADD CONSTRAINT "FK_9de8d585bd7169e22bd6d085c78" FOREIGN KEY ("user_who_paid_id") REFERENCES "currency"("id") ON DELETE NO ACTION ON UPDATE NO ACTION
        `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
            ALTER TABLE "expense" DROP CONSTRAINT "FK_9de8d585bd7169e22bd6d085c78"
        `);
    await queryRunner.query(`
            ALTER TABLE "user" DROP CONSTRAINT "FK_4245a6b002b13f12e426d9db3ff"
        `);
    await queryRunner.query(`
            ALTER TABLE "expense" DROP COLUMN "user_who_paid_id"
        `);
    await queryRunner.query(`
            ALTER TABLE "expense" DROP COLUMN "description"
        `);
    await queryRunner.query(`
            ALTER TABLE "expense"
            ADD "user_who_paid" character varying NOT NULL
        `);
    await queryRunner.query(`
            ALTER TABLE "expense"
            ADD "amount" numeric(10, 2) NOT NULL
        `);
    await queryRunner.query(`
            ALTER TABLE "expense"
            ADD "name" character varying NOT NULL
        `);
    await queryRunner.query(`
            ALTER TABLE "event"
            ADD "users" jsonb NOT NULL
        `);
    await queryRunner.query(`
            ALTER TABLE "event"
            ADD "owner_id" character varying NOT NULL
        `);
    await queryRunner.query(`
            DROP TABLE "user"
        `);
  }
}
