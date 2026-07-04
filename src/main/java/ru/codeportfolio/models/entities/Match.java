package ru.codeportfolio.models.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table (name = "matches",
        check = @CheckConstraint(
                name = "notnull",
                constraint = "winner = player_1 OR winner = player_2 OR winner IS NULL"))
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

        public Match(Player homePlayer, Player guestPlayer) {
                this.homePlayer = homePlayer;
                this.guestPlayer = guestPlayer;
        }

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
}
