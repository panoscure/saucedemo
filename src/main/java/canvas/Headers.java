package canvas;

import common.utils.Properties;
import lottery.apigatewayj.requests.wagers.it.is.used.to.play.a.wager.*;

import java.util.*;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

public class Headers {

    public static Integer megamillionsGame = Integer.valueOf(Properties.getPropertyValue("megamillions.id"));
    public static Map<String, String> getCommonHeaders(String bearerToken) {
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Guid", UUID.randomUUID().toString());
        headers.put(AUTHORIZATION, "Bearer " + bearerToken);
        headers.put("Content-Type", "application/json");
        headers.put("accept", "application/json");
        headers.put("Operator", Properties.getPropertyValue("operator"));
        headers.put("Channel", Properties.getPropertyValue("web.channel"));
        return headers;
    }

    public static WagerBuilderV2 playMM() {
        List<Integer> multipliers = new ArrayList<>(Arrays.asList(1));

        List<Panel> panels = new ArrayList<>();
        Panel firstPanel = new Panel().withRequested(5).withSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5))).withQuickPick(false);
        Panel secondPanel = new Panel().withRequested(1).withSelection(new ArrayList<>(Arrays.asList(1))).withQuickPick(false);
        panels.add(firstPanel);
        panels.add(secondPanel);

        List<Board> boards = new ArrayList<>();
        Board board = new Board().withBoardId(1).withMultipliers(multipliers).withPanels(panels);
        boards.add(board);

        ParticipatingDraws participatingDraws = new ParticipatingDraws().withMultipleDraws(1).withDrawOffsets(new ArrayList<>(Arrays.asList(0)));

        List<Dbg> dbgs = new ArrayList<>();
        Dbg dbg = new Dbg()
                .withBoards(boards)
                .withGameId(megamillionsGame)
                .withTeamShares(0)
                .withMultipliers(new ArrayList<>())
                .withParticipatingDraws(participatingDraws)
                .withQuickPick(false);
        dbgs.add(dbg);

        Wager wager = new Wager();
        wager.withDbg(dbgs);

        Metadata metadata = new Metadata();
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.withSessionToken("sessionToken");
        playerInfo.withPlayerId(1234);
        metadata.withPlayerInfo(playerInfo);

        Options options = new Options();
        options.setFirstname("First Name");
        options.setEmail("a@a.gr");
        metadata.setOptions(options);

        return WagerBuilderV2.withMetadataNoIsecure(metadata).withWager(wager);
    }

}
