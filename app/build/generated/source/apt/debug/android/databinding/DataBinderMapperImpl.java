package android.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.dm.crmdm_app.DataBinderMapperImpl());
  }
}
