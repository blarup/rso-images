package si.thoughts.texts;

import com.kumuluz.ee.grpc.client.GrpcChannelConfig;
import com.kumuluz.ee.grpc.client.GrpcChannels;
import com.kumuluz.ee.grpc.client.GrpcClient;
import io.grpc.stub.StreamObserver;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.SSLException;

@ApplicationScoped
public class CommentsGrpcService {
    private CommentsGrpc.CommentsStub stub;

    @PostConstruct
    public void init(){
        try{
            GrpcChannels clientPool = GrpcChannels.getInstance();
            GrpcChannelConfig config = clientPool.getGrpcClientConfig("client1");
            GrpcClient client = new GrpcClient(config);
            stub = CommentsGrpc.newStub(client.getChannel());
        } catch (SSLException e)
        {
            System.err.println(e);
        }
    }

    public void commentCleanUp(Integer id){
        CommentsService.CleanUpRequest request = CommentsService.CleanUpRequest.newBuilder().setId(id).build();

        stub.commentCleanUp(request, new StreamObserver<CommentsService.CleanUpResponse>() {
            @Override
            public void onNext(CommentsService.CleanUpResponse cleanUpResponse) {
                if(cleanUpResponse.getStatus() != 0){
                    System.err.println("Comment clean up error");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
            }
        });
    }
}
