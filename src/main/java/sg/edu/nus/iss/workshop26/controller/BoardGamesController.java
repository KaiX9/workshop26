package sg.edu.nus.iss.workshop26.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonObject;
import sg.edu.nus.iss.workshop26.model.Game;
import sg.edu.nus.iss.workshop26.model.Games;
import sg.edu.nus.iss.workshop26.repository.BoardGamesRepository;

@RestController
@RequestMapping
public class BoardGamesController {
    
    @Autowired
    private BoardGamesRepository bgRepo;
    // To fetch all board games

    @GetMapping(path="/games")
    public ResponseEntity<String> getAllBoardGames(@RequestParam(required=false) Integer limit
        , @RequestParam(required=false) Integer offset) {

        if (limit == null) {
            limit = 25;
        }

        if (offset == null) {
            offset = 0;
        }

        List<Game> gameList = bgRepo.getAllBoardGames(limit, offset);

        Games games = new Games();
        games.setGameList(gameList);
        games.setOffset(offset);
        games.setLimit(limit);
        games.setTotal(gameList.size());
        games.setTimeStamp(LocalDateTime.now());

        JsonObject result = games.toJSON();

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(result.toString());
    }

    @GetMapping(path="/games/rank")
    public ResponseEntity<String> getSortedByRank(@RequestParam(required=false) Integer limit
        , @RequestParam(required=false) Integer offset) {

        if (limit == null) {
            limit = 25;
        }
    
        if (offset == null) {
            offset = 0;
        }

        List<Game> gameListByRank = bgRepo.getSortedByRank(limit, offset);
        
        Games games = new Games();
        games.setGameList(gameListByRank);
        games.setOffset(offset);
        games.setLimit(limit);
        games.setTotal(gameListByRank.size());
        games.setTimeStamp(LocalDateTime.now());

        JsonObject result = games.toJSON();

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(result.toString());
    }

    @GetMapping(path="/games/{gameId}")
    public ResponseEntity<String> getBoardGameById(@PathVariable String gameId) {

        try {
        Game game = bgRepo.getBoardGameById(gameId);

        JsonObject result = game.toJSON();

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(result.toString());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("Game ID of %s not found".formatted(gameId));
        }
    }
}
