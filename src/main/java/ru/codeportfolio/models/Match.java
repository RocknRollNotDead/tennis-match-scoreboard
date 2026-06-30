package ru.codeportfolio.models;

import jakarta.persistence.*;

@Entity
@Table (name = "matches")
public class Match {

        @Id
        private long id;

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

        public Match(long id, Player homePlayer, Player guestPlayer, Player winner) {
                this.id = id;
                this.homePlayer = homePlayer;
                this.guestPlayer = guestPlayer;
                this.winner = winner;
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
}
