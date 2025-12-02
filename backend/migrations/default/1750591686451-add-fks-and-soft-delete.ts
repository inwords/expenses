import {MigrationInterface, QueryRunner} from "typeorm";

export class AddFksAndSoftDelete1750591686451 implements MigrationInterface {
    name = 'AddFksAndSoftDelete1750591686451'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "event" ADD "deleted_at" TIMESTAMP WITH TIME ZONE`);
        await queryRunner.query(`ALTER TABLE "event" ADD CONSTRAINT "fk__event__currency_id" FOREIGN KEY ("currency_id") REFERENCES "currency"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "user" ADD CONSTRAINT "fk__user__event_id" FOREIGN KEY ("event_id") REFERENCES "event"("id") ON DELETE CASCADE ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "expense" ADD CONSTRAINT "fk__expense__event_id" FOREIGN KEY ("event_id") REFERENCES "event"("id") ON DELETE CASCADE ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "expense" ADD CONSTRAINT "fk__expense__user_who_paid_id" FOREIGN KEY ("user_who_paid_id") REFERENCES "user"("id") ON DELETE CASCADE ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "expense" ADD CONSTRAINT "fk__expense__currency_id" FOREIGN KEY ("currency_id") REFERENCES "currency"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "expense" DROP CONSTRAINT "fk__expense__currency_id"`);
        await queryRunner.query(`ALTER TABLE "expense" DROP CONSTRAINT "fk__expense__user_who_paid_id"`);
        await queryRunner.query(`ALTER TABLE "expense" DROP CONSTRAINT "fk__expense__event_id"`);
        await queryRunner.query(`ALTER TABLE "user" DROP CONSTRAINT "fk__user__event_id"`);
        await queryRunner.query(`ALTER TABLE "event" DROP CONSTRAINT "fk__event__currency_id"`);
        await queryRunner.query(`ALTER TABLE "event" DROP COLUMN "deleted_at"`);
    }
}
