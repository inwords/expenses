import {Injectable} from '@nestjs/common';
import * as bcrypt from 'bcrypt';
import {ConfigService} from '@nestjs/config';

@Injectable()
export class HashingService {
  constructor(private readonly configService: ConfigService) {}
  public async getHash(data: string): Promise<string> {
    return await bcrypt.hash(data, Number(this.configService.get('SALT_OR_ROUNDS')));
  }

  public async compare(data: string, hash: string): Promise<boolean> {
    return await bcrypt.compare(data, hash);
  }
}
