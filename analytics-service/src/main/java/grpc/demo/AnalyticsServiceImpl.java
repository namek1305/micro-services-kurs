package grpc.demo;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * Пример gRPC-сервиса, рассчитывающего футбольный рейтинг игрока.
 * (Обратите внимание: имена типов запроса/ответа предполагают изменение .proto — адаптируйте под ваши .proto)
 */
@GrpcService
public class AnalyticsServiceImpl extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {

    @Override
    public void calculatePlayerRating(PlayerRatingRequest request, StreamObserver<PlayerRatingResponse> responseObserver) {
        // Заглушка: здесь должна быть реальная логика расчёта рейтинга по матчам / метрикам
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
