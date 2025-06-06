package com.movie.data.domain;

import com.movie.common.annotation.Excel;
import com.movie.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class MovieShow extends BaseEntity {
    /** id */
    private Long id;

    /** 电影名字 */
    @Excel(name = "电影名字")
    private String name;

    /** 上市时间 **/
    @Excel(name = "上市时间")
    private Integer releaseDate;

    /** 电影地区 */
    @Excel(name = "电影地区")
    private Long areaId;

    /** 电影类型 */
    @Excel(name = "电影类型")
    private Long genreId;

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
    @Excel(name = "vip")
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

    /** 主演 **/
    @Excel(name = "主演")
    private List<Actors> actorList;

    /** 导演 **/
    @Excel(name = "导演")
    private List<Directors> directorList;


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setReleaseDate(Integer releaseDate) {
        this.releaseDate = releaseDate;
    }
    public Integer getReleaseDate() {
        return releaseDate;
    }

    public void setAreaId(Long areaId)
    {
        this.areaId = areaId;
    }

    public Long getAreaId()
    {
        return areaId;
    }

    public void setGenreId(Long genreId)
    {
        this.genreId = genreId;
    }

    public Long getGenreId()
    {
        return genreId;
    }

    public void setCount(Long count)
    {
        this.count = count;
    }

    public Long getCount()
    {
        return count;
    }

    public void setWeekcount(Long weekcount)
    {
        this.weekcount = weekcount;
    }

    public Long getWeekcount()
    {
        return weekcount;
    }

    public void setMonthcount(Long monthcount)
    {
        this.monthcount = monthcount;
    }

    public Long getMonthcount()
    {
        return monthcount;
    }

    public void setSee(Integer see)
    {
        this.see = see;
    }

    public Integer getSee()
    {
        return see;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setScoreTotal(Long scoreTotal)
    {
        this.scoreTotal = scoreTotal;
    }

    public Long getScoreTotal()
    {
        return scoreTotal;
    }

    public void setScoreCount(Long scoreCount)
    {
        this.scoreCount = scoreCount;
    }

    public Long getScoreCount()
    {
        return scoreCount;
    }

    public void setCoverurl(String coverurl)
    {
        this.coverurl = coverurl;
    }

    public String getCoverurl()
    {
        return coverurl;
    }

    public List<Actors> getActorList() {
        return actorList;
    }

    public void setActorList(List<Actors> actorList) {
        this.actorList = actorList;
    }

    public List<Directors> getDirectorList() {
        return directorList;
    }

    public void setDirectorList(List<Directors> directorList) {
        this.directorList = directorList;
    }

    public String getMovieurl() {
        return movieurl;
    }

    public void setMovieurl(String movieurl) {
        this.movieurl = movieurl;
    }

    @Override
    public String toString() {
        return "MovieShow{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", areaId=" + areaId +
                ", genreId=" + genreId +
                ", count=" + count +
                ", weekcount=" + weekcount +
                ", monthcount=" + monthcount +
                ", see=" + see +
                ", description='" + description + '\'' +
                ", scoreTotal=" + scoreTotal +
                ", scoreCount=" + scoreCount +
                ", coverurl='" + coverurl + '\'' +
                ", actorList=" + actorList +
                ", directorList=" + directorList +
                ", movieurl='" + movieurl + '\'' +
                '}';
    }
}
