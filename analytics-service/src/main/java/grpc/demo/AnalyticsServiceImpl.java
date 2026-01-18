package grpc.demo;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AnalyticsServiceImpl extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {

    @Override
    public void calculatePlayerRating(PlayerRatingRequest request, StreamObserver<PlayerRatingResponse> responseObserver) {
        int score = (int) (Math.random() * 100);
        String verdict = score > 60 ? "EXCELLENT" : (score > 40 ? "GOOD" : "POOR");

        PlayerRatingResponse response = PlayerRatingResponse.newBuilder()
                .setPlayerId(request.getPlayerId())
                .setMatchId(request.getMatchId())
                .setRatingScore(score)
                .setVerdict(verdict)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
