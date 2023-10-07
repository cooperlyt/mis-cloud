package io.github.cooperlyt.commons.cloud.elasticsearch;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DataPage<T> implements java.io.Serializable{

  public DataPage(List<T> data, boolean isExactResult, int pageNum, int pageSize, long total){
    this.list = data;
    this.total = total;
    this.pageNum = Math.max(pageNum, 1);
    this.pageSize = pageSize;
    this.isExactResult = isExactResult;

    this.size = this.list.size();
    this.startRow = Math.max((this.pageNum - 1) * this.pageSize, 0);
    this.endRow = this.startRow + this.size - 1;

    this.pages = (int)Math.ceil((double)this.total/this.pageSize);
    this.prePage = Math.max(this.pageNum - 1,1);
    this.nextPage = Math.min(this.pageNum + 1, this.pages);
    this.isFirstPage = this.pageNum == 1;
    this.isLastPage = this.pageNum == this.pages;
    this.hasNextPage = this.pageNum < this.pages;
    this.hasPreviousPage = this.pageNum > 1;
  }

  //总记录数
  protected long total;
  //结果集
  protected List<T> list;

  /**
   * 当前页
   */
  private int pageNum;
  /**
   * 每页的数量
   */
  private int pageSize;
  /**
   * 当前页的数量
   */
  private int size;

  /**
   * 由于startRow和endRow不常用，这里说个具体的用法
   * 可以在页面中"显示startRow到endRow 共size条数据"
   * 当前页面第一个元素在数据库中的行号
   */
  private long startRow;
  /**
   * 当前页面最后一个元素在数据库中的行号
   */
  private long endRow;
  /**
   * 总页数
   */
  private int pages;
  /**
   * 前一页
   */
  private int prePage;
  /**
   * 下一页
   */
  private int nextPage;

  @JsonProperty("isExactResult")
  private boolean isExactResult;

  /**
   * 是否为第一页
   */
  @JsonProperty("isFirstPage")
  private boolean isFirstPage = false;
  /**
   * 是否为最后一页
   */
  @JsonProperty("isLastPage")
  private boolean isLastPage = false;
  /**
   * 是否有前一页
   */
  private boolean hasPreviousPage = false;
  /**
   * 是否有下一页
   */
  private boolean hasNextPage = false;

}
