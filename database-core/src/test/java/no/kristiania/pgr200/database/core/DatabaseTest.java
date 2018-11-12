package no.kristiania.pgr200.database.core;


import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseTest {


    @Test
    public void insertAndShowTalk() throws SQLException {
        DataSource dataSource = createDataSource();
        Database db = new Database(dataSource);
        Talk testTalk = sampleTalk();

        db.insertTalk(testTalk);
        System.out.println(testTalk);
        assertThat(db.getTalk(testTalk.getId())).isEqualToComparingFieldByField(testTalk);
    }

    @Test
    public void listTalks() {
        DataSource dataSource = createDataSource();
        Database db = new Database(dataSource);
        Talk testTalk = sampleTalk();
        db.insertTalk(testTalk);
        System.out.println(db.listAll());
        assertThat((db.listAll())).isNotEmpty();
    }

    @Test 
    public void updateTalk(){
        DataSource dataSource = createDataSource();
        Database db = new Database(dataSource);
        Talk testTalk = sampleTalk();
        Map<String, String> fakeArgs = new HashMap<>();

        db.insertTalk(testTalk);
        fakeArgs.put("id",String.valueOf(testTalk.getId()));
        fakeArgs.put("title", "5");
        db.updateTalk(fakeArgs);
        assertThat(db.getTalk(testTalk.getId()).getTitle()).isEqualToIgnoringCase("5");
    }

    private Talk sampleTalk() {
        Talk talk = new Talk();
        talk.setTitle("1");
        talk.setDescription("2");
        talk.setTopic("3");
        return talk;
    }

    private DataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;

    }

}
