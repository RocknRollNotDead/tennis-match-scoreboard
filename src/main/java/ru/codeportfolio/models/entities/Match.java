package ru.codeportfolio.models.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.util.Objects;

@Entity
@Table (name = "matches",
        check = {
        @CheckConstraint(
                        name = "notnull",
                        constraint = "winner = player_1 OR winner = player_2 OR winner IS NULL"),
        @CheckConstraint(
                        name = "player1_not_player2",
                        constraint = "player_1 != player_2")
})
public class Match {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "player_1", nullable = false)
        private Player homePlayer;

        @ManyToOne
        @JoinColumn(name = "player_2", nullable = false)
        private Player guestPlayer;

        @ManyToOne
        @JoinColumn(name = "winner")
        private Player winner;

        public Match() {
        }

        public Match(Player homePlayer, Player guestPlayer, Player winner) {
                this.homePlayer = homePlayer;
                this.guestPlayer = guestPlayer;
                this.winner = winner;
        }

        /*public Match(Player homePlayer, Player guestPlayer) {
                this.homePlayer = homePlayer;
                this.guestPlayer = guestPlayer;
        }
*/
        public long getId() {
                return id;
        }

        public Player getHomePlayer() {
                return homePlayer;
        }

        public Player getGuestPlayer() {
                return guestPlayer;
        }

        public Player getWinner() {
                return winner;
        }

        public void setWinner(Player winner) {
                this.winner = winner;
        }

        @Override
        public String toString() {
                return "Match{" +
                        "id=" + id +
                        ", homePlayer=" + homePlayer +
                        ", guestPlayer=" + guestPlayer +
                        ", winner=" + winner +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                Match match = (Match) o;
                return Objects.equals(id, match.id) && Objects.equals(homePlayer, match.homePlayer) && Objects.equals(guestPlayer, match.guestPlayer) && Objects.equals(winner, match.winner);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, homePlayer, guestPlayer, winner);
        }
}
