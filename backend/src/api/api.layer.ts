import {Module} from "@nestjs/common";
import {HttpModule} from "#api/http/http.module";
import {GrpcModule} from "#api/grpc/grpcModule";

@Module({
    imports: [HttpModule, GrpcModule],
    controllers: [],
    providers: [],
})
export class ApiModule {
}