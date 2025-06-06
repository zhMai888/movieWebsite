package com.movie.data.domain;

import com.movie.common.annotation.Excel;

public class MovieExportVO {
    /** id */
    @Excel(name = "电影ID")
    private Long id;

    /** 电影名字 */
    @Excel(name = "电影名字")
    private String name;

    /** 上市时间 **/
    @Excel(name = "上市时间")
    private Integer releaseDate;

    @Excel(name = "主演")
    private String actors;

    @Excel(name = "导演")
    private String directors;

    /** 电影地区 */
    @Excel(name = "电影地区")
    private String areaName;

    /** 电影类型 */
    @Excel(name = "电影类型")
    private String genreName;

    /** 观看次数 */
    @Excel(name = "观看次数")
    private Long count;

    /** 本周观看次数 */
    @Excel(name = "本周观看次数")
    private Long weekcount;

    /** 月观看次数 */
    @Excel(name = "月观看次数")
    private Long monthcount;

    /** vip */
    @Excel(name = "vip", readConverterExp = "0=否,1=是")
    private Integer see;

    /** 电影描述 */
    @Excel(name = "电影描述")
    private String description;

    /** 评分 */
    @Excel(name = "评分")
    private Long scoreTotal;

    /** 评分人数 */
    @Excel(name = "评分人数")
    private Long scoreCount;

    /** 封面url */
    @Excel(name = "封面url")
    private String coverurl;

    /** 资源地址 **/
    @Excel(name = "资源地址")
    private String movieurl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Integer releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getWeekcount() {
        return weekcount;
    }

    public void setWeekcount(Long weekcount) {
        this.weekcount = weekcount;
    }

    public Long getMonthcount() {
        return monthcount;
    }

    public void setMonthcount(Long monthcount) {
        this.monthcount = monthcount;
    }

    public Integer getSee() {
        return see;
    }

    public void setSee(Integer see) {
        this.see = see;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(Long scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public Long getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(Long scoreCount) {
        this.scoreCount = scoreCount;
    }

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }

    public String getMovieurl() {
        return movieurl;
    }

    public void setMovieurl(String movieurl) {
        this.movieurl = movieurl;
    }

    @Override
    public String toString() {
        return "MovieExportVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", actors='" + actors + '\'' +
                ", directors='" + directors + '\'' +
                ", areaName='" + areaName + '\'' +
                ", genreName='" + genreName + '\'' +
                ", count=" + count +
                ", weekcount=" + weekcount +
                ", monthcount=" + monthcount +
                ", see=" + see +
                ", description='" + description + '\'' +
                ", scoreTotal=" + scoreTotal +
                ", scoreCount=" + scoreCount +
                ", coverurl='" + coverurl + '\'' +
                ", movieurl='" + movieurl + '\'' +
                '}';
    }
}
