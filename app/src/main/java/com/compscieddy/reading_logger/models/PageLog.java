package com.compscieddy.reading_logger.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by elee on 1/12/16.
 */
@ParseClassName("PageLog")
public class PageLog extends ParseObject {

  private static final String PAGE_NUM = "page_num";
  // .getUpdatedAt() and .getCreatedAt()

  public int getPageNum() {
    return getInt(PAGE_NUM);
  }

  public void setPageNum(int pageNum) {
    put(PAGE_NUM, pageNum);
  }

  public static ParseQuery<PageLog> getQuery() {
    return ParseQuery.getQuery(PageLog.class);
  }

}
