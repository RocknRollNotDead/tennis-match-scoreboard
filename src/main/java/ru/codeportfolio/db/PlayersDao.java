package ru.codeportfolio.db;


import ru.codeportfolio.exceptions.CurrencyAlreadyExistException;
import ru.codeportfolio.exceptions.DataAccessException;
import ru.codeportfolio.models.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayersDao implements PlayersDaoInterface {



    private final Connection conn;

    public PlayersDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Player> getAll() {
        return List.of();
    }

    @Override
    public int add(String name) {
        return 0;
    }

    @Override
    public Player findByName(String name) {
        return null;
    }

    @Override
    public int delete(String name) {
        return 0;
    }



/*
    @Override
    public List<Player> getAll() {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, name, full_name, sign FROM currencies")){
            List<Player> currencies = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Player player = new Player(rs.getInt("id"),
                        rs.getString("name")
                );
                currencies.add(player);
            }
            return currencies;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch currencies", e);
        }
    }

    @Override
    public int add(String name) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO currencies(name) VALUES (?, ?, ?);"
        )){
            stmt.setString(1, name);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            if (!isCurrencyAlreadyExist(e)){
                throw new DataAccessException("Failed to add currency", e);
            }

            throw new CurrencyAlreadyExistException("Failed to add currency", e);

        }
    }

    @Override
    public Player findByName(String name){

        try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, name, full_name, sign FROM currencies WHERE name = ?"
            )){
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Player(rs.getInt("id"),
                        rs.getString("name")

                );
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get currency", e);
        }
    }


    @Override
    public int delete(String name) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM currencies WHERE name = ?"
        )){

            stmt.setString(1, name);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete currency", e);
        }
    }

    private boolean isCurrencyAlreadyExist(SQLException e) {
        return e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed");
    }
*/
}

