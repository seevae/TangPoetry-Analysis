package io.github.seevae.crawler.pipeline;

import io.github.seevae.crawler.common.Page;
import io.github.seevae.crawler.common.PoetryInfo;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据写入数据库
 */
public class DatabasePipeline implements Pipeline {

    private final DataSource dataSource;

    public DatabasePipeline(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void pipeline(Page page) {
        PoetryInfo poetryInfo = (PoetryInfo) page.getDataSet().getData("poetry");

        String sql = "insert into poetry_info (title, dynasty, author, content) values (?,?,?,?)";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
           statement.setString(1,poetryInfo.getTitle());
           statement.setString(2,poetryInfo.getDynasty());
           statement.setString(3,poetryInfo.getAuthor());
           statement.setString(4,poetryInfo.getContent());

           statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
