package com.musinsa.api_task.domain.config;

import com.musinsa.api_task.common.constants.FileConstants;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;

import java.util.Locale;

public class P6spySqlFormatter implements MessageFormattingStrategy {
    private static final String CREATE = "create";
    private static final String ALTER = "alter";
    private static final String COMMENT = "comment";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (sql.trim().isEmpty()) {
            return "";
        }

        return sqlFormat(sql, category);
    }

    private String sqlFormat(final String sql, final String category) {
        if (isStatementDdl(sql, category)) {
            return FormatStyle.DDL
                    .getFormatter()
                    .format(sql);
        }

        return FormatStyle.BASIC
                .getFormatter()
                .format(sql);
    }

    private boolean isStatementDdl(final String sql, final String category) {
        return isStatement(category) && isDdl(sql);
    }

    private boolean isStatement(final String category) {
        return Category.STATEMENT.getName().equals(category);
    }

    private boolean isDdl(final String sql) {
        final String lowerSql = sql.trim().toLowerCase(Locale.ROOT);
        return lowerSql.startsWith(CREATE) || lowerSql.startsWith(ALTER) || lowerSql.startsWith(COMMENT);
    }

}
