package com.serverless.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ObjectMapperの余分な再生成抑制と、例外発生時のハンドリングを一元管理する。
 */
public class ObjectMapperUtil {

  private static final ObjectMapper mapper = new ObjectMapper();
  private static Logger LOG = LogManager.getLogger(ObjectMapperUtil.class);


  /**
   * @return mapper
   */
  public static ObjectMapper getMapper() {
    mapper.registerModule(getModule());
    return mapper;
  }

  /**
   * JSONからEntityに変換する
   *
   * @param contentJson クラス型にマッピングしたいJSON
   * @param valueType JSONをマッピングしたいクラス型
   * @return JSONがマッピングされた第二引数に指定したクラスのインスタンス
   * @throws IOException Jsonのread失敗時
   */
  @Nullable
  public static <T> T readValue(@Nullable String contentJson, @NonNull Class<T> valueType)
      throws IOException {
    if (contentJson == null) {
      LOG.warn("引数不正の為終了. content = null.");
      return null;
    }

    try {
      return getMapper().readValue(contentJson, valueType);
    } catch (IOException e) {
      LOG.warn("Jsonのreadに失敗.", e);
      throw e;
    }
  }

  /**
   * JSONからEntityのリストに変換する
   *
   * @param listContentJson クラス型にマッピングしたいリストのJSON
   * @param valueListType JSONをマッピングしたいリストのクラス型
   * @return JSONがマッピングされた第二引数に指定したクラスのインスタンスのリスト
   * @throws IOException Jsonのread失敗時
   */
  @Nullable
  public static <T> List<T> readValue(@Nullable String listContentJson,
      @Nullable TypeReference<List<T>> valueListType)
      throws IOException {
    if (listContentJson == null || valueListType == null) {
      LOG.warn(String.format("引数不正の為終了. content = %1$s, valueListType = %2$s ",
          listContentJson, valueListType));
      return null;
    }

    try {
      return getMapper().readValue(listContentJson, valueListType);
    } catch (IOException e) {
      LOG.warn("Jsonのreadに失敗. e = {}.", e);
      throw e;
    }
  }

  /**
   * JSONからマップに変換する
   *
   * @param mapContentJson クラス型にマッピングしたいマップのJSON
   * @param valueMapType JSONをマッピングしたいマップのクラス型
   * @return JSONがマッピングされた第二引数に指定したクラスのインスタンスのマップ
   * @throws IOException Jsonのread失敗時
   */
  @Nullable
  public static <T1, T2> Map<T1, T2> readValueAsMap(@Nullable String mapContentJson,
      @Nullable TypeReference<Map<T1, T2>> valueMapType) {
    if (mapContentJson == null || valueMapType == null) {
      LOG.warn(String.format("引数不正の為終了. content = %1$s, valueListType = %2$s ",
          mapContentJson, valueMapType));
      return null;
    }

    try {
      return getMapper().readValue(mapContentJson, valueMapType);
    } catch (IOException e) {
      LOG.warn("Jsonのreadに失敗. Exception = ", e);
      LOG.info("String = {}", mapContentJson);
      return null;
    }
  }

  /**
   * ファイルからJsonNodeに変換する
   *
   * @param file 読み込むファイル
   * @return JsonNode fileから読み込んで変換されたJsonNode
   * @throws IOException JsonNode構築失敗時
   */
  @Nullable
  public static JsonNode readTree(@Nullable File file) throws IOException {
    if (file == null) {
      LOG.warn("引数不正の為終了. file = null.");
      return null;
    }

    try {
      return getMapper().readTree(file);
    } catch (IOException e) {
      LOG.warn("JsonNodeの構築に失敗. e = {}.", e);
      throw e;
    }
  }

  /**
   * ObjectからJSONに変換する
   *
   * @param valueObj 値オブジェクト
   * @return 変換されたJSON文字列
   * @throws IOException 値オブジェクトのwrite失敗時
   */
  @Nullable
  public static String writeValueAsString(@Nullable Object valueObj) throws IOException {
    if (valueObj == null) {
      LOG.warn("引数不正の為終了. Value = null.");
      return null;
    }

    try {
      return getMapper().writeValueAsString(valueObj);
    } catch (IOException e) {
      LOG.warn("Jsonの文字列化に失敗. e = {}.", e);
      throw e;
    }
  }

  /**
   * @return module
   */
  @NonNull
  private static SimpleModule getModule() {
    SimpleModule module = new SimpleModule();
    module.addSerializer(new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyyMMdd")));
    module
        .addSerializer(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
    return module;
  }
}
