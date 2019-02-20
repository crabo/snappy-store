/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package io.snappydata.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2019-02-20")
public class CatalogPartitionObject implements org.apache.thrift.TBase<CatalogPartitionObject, CatalogPartitionObject._Fields>, java.io.Serializable, Cloneable, Comparable<CatalogPartitionObject> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("CatalogPartitionObject");

  private static final org.apache.thrift.protocol.TField SPEC_FIELD_DESC = new org.apache.thrift.protocol.TField("spec", org.apache.thrift.protocol.TType.MAP, (short)1);
  private static final org.apache.thrift.protocol.TField STORAGE_FIELD_DESC = new org.apache.thrift.protocol.TField("storage", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField PARAMETERS_FIELD_DESC = new org.apache.thrift.protocol.TField("parameters", org.apache.thrift.protocol.TType.MAP, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new CatalogPartitionObjectStandardSchemeFactory());
    schemes.put(TupleScheme.class, new CatalogPartitionObjectTupleSchemeFactory());
  }

  public Map<String,String> spec; // required
  public CatalogStorage storage; // required
  public Map<String,String> parameters; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    SPEC((short)1, "spec"),
    STORAGE((short)2, "storage"),
    PARAMETERS((short)3, "parameters");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // SPEC
          return SPEC;
        case 2: // STORAGE
          return STORAGE;
        case 3: // PARAMETERS
          return PARAMETERS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SPEC, new org.apache.thrift.meta_data.FieldMetaData("spec", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.STORAGE, new org.apache.thrift.meta_data.FieldMetaData("storage", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, CatalogStorage.class)));
    tmpMap.put(_Fields.PARAMETERS, new org.apache.thrift.meta_data.FieldMetaData("parameters", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(CatalogPartitionObject.class, metaDataMap);
  }

  public CatalogPartitionObject() {
  }

  public CatalogPartitionObject(
    Map<String,String> spec,
    CatalogStorage storage,
    Map<String,String> parameters)
  {
    this();
    this.spec = spec;
    this.storage = storage;
    this.parameters = parameters;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public CatalogPartitionObject(CatalogPartitionObject other) {
    if (other.isSetSpec()) {
      Map<String,String> __this__spec = new HashMap<String,String>(other.spec);
      this.spec = __this__spec;
    }
    if (other.isSetStorage()) {
      this.storage = new CatalogStorage(other.storage);
    }
    if (other.isSetParameters()) {
      Map<String,String> __this__parameters = new HashMap<String,String>(other.parameters);
      this.parameters = __this__parameters;
    }
  }

  public CatalogPartitionObject deepCopy() {
    return new CatalogPartitionObject(this);
  }

  @Override
  public void clear() {
    this.spec = null;
    this.storage = null;
    this.parameters = null;
  }

  public int getSpecSize() {
    return (this.spec == null) ? 0 : this.spec.size();
  }

  public void putToSpec(String key, String val) {
    if (this.spec == null) {
      this.spec = new HashMap<String,String>();
    }
    this.spec.put(key, val);
  }

  public Map<String,String> getSpec() {
    return this.spec;
  }

  public CatalogPartitionObject setSpec(Map<String,String> spec) {
    this.spec = spec;
    return this;
  }

  public void unsetSpec() {
    this.spec = null;
  }

  /** Returns true if field spec is set (has been assigned a value) and false otherwise */
  public boolean isSetSpec() {
    return this.spec != null;
  }

  public void setSpecIsSet(boolean value) {
    if (!value) {
      this.spec = null;
    }
  }

  public CatalogStorage getStorage() {
    return this.storage;
  }

  public CatalogPartitionObject setStorage(CatalogStorage storage) {
    this.storage = storage;
    return this;
  }

  public void unsetStorage() {
    this.storage = null;
  }

  /** Returns true if field storage is set (has been assigned a value) and false otherwise */
  public boolean isSetStorage() {
    return this.storage != null;
  }

  public void setStorageIsSet(boolean value) {
    if (!value) {
      this.storage = null;
    }
  }

  public int getParametersSize() {
    return (this.parameters == null) ? 0 : this.parameters.size();
  }

  public void putToParameters(String key, String val) {
    if (this.parameters == null) {
      this.parameters = new HashMap<String,String>();
    }
    this.parameters.put(key, val);
  }

  public Map<String,String> getParameters() {
    return this.parameters;
  }

  public CatalogPartitionObject setParameters(Map<String,String> parameters) {
    this.parameters = parameters;
    return this;
  }

  public void unsetParameters() {
    this.parameters = null;
  }

  /** Returns true if field parameters is set (has been assigned a value) and false otherwise */
  public boolean isSetParameters() {
    return this.parameters != null;
  }

  public void setParametersIsSet(boolean value) {
    if (!value) {
      this.parameters = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case SPEC:
      if (value == null) {
        unsetSpec();
      } else {
        setSpec((Map<String,String>)value);
      }
      break;

    case STORAGE:
      if (value == null) {
        unsetStorage();
      } else {
        setStorage((CatalogStorage)value);
      }
      break;

    case PARAMETERS:
      if (value == null) {
        unsetParameters();
      } else {
        setParameters((Map<String,String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case SPEC:
      return getSpec();

    case STORAGE:
      return getStorage();

    case PARAMETERS:
      return getParameters();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case SPEC:
      return isSetSpec();
    case STORAGE:
      return isSetStorage();
    case PARAMETERS:
      return isSetParameters();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof CatalogPartitionObject)
      return this.equals((CatalogPartitionObject)that);
    return false;
  }

  public boolean equals(CatalogPartitionObject that) {
    if (that == null)
      return false;

    boolean this_present_spec = true && this.isSetSpec();
    boolean that_present_spec = true && that.isSetSpec();
    if (this_present_spec || that_present_spec) {
      if (!(this_present_spec && that_present_spec))
        return false;
      if (!this.spec.equals(that.spec))
        return false;
    }

    boolean this_present_storage = true && this.isSetStorage();
    boolean that_present_storage = true && that.isSetStorage();
    if (this_present_storage || that_present_storage) {
      if (!(this_present_storage && that_present_storage))
        return false;
      if (!this.storage.equals(that.storage))
        return false;
    }

    boolean this_present_parameters = true && this.isSetParameters();
    boolean that_present_parameters = true && that.isSetParameters();
    if (this_present_parameters || that_present_parameters) {
      if (!(this_present_parameters && that_present_parameters))
        return false;
      if (!this.parameters.equals(that.parameters))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_spec = true && (isSetSpec());
    list.add(present_spec);
    if (present_spec)
      list.add(spec);

    boolean present_storage = true && (isSetStorage());
    list.add(present_storage);
    if (present_storage)
      list.add(storage);

    boolean present_parameters = true && (isSetParameters());
    list.add(present_parameters);
    if (present_parameters)
      list.add(parameters);

    return list.hashCode();
  }

  @Override
  public int compareTo(CatalogPartitionObject other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetSpec()).compareTo(other.isSetSpec());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSpec()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.spec, other.spec);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetStorage()).compareTo(other.isSetStorage());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStorage()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.storage, other.storage);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetParameters()).compareTo(other.isSetParameters());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParameters()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.parameters, other.parameters);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("CatalogPartitionObject(");
    boolean first = true;

    sb.append("spec:");
    if (this.spec == null) {
      sb.append("null");
    } else {
      sb.append(this.spec);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("storage:");
    if (this.storage == null) {
      sb.append("null");
    } else {
      sb.append(this.storage);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("parameters:");
    if (this.parameters == null) {
      sb.append("null");
    } else {
      sb.append(this.parameters);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (spec == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'spec' was not present! Struct: " + toString());
    }
    if (storage == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'storage' was not present! Struct: " + toString());
    }
    if (parameters == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'parameters' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (storage != null) {
      storage.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class CatalogPartitionObjectStandardSchemeFactory implements SchemeFactory {
    public CatalogPartitionObjectStandardScheme getScheme() {
      return new CatalogPartitionObjectStandardScheme();
    }
  }

  private static class CatalogPartitionObjectStandardScheme extends StandardScheme<CatalogPartitionObject> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, CatalogPartitionObject struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // SPEC
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map400 = iprot.readMapBegin();
                struct.spec = new HashMap<String,String>(2*_map400.size);
                String _key401;
                String _val402;
                for (int _i403 = 0; _i403 < _map400.size; ++_i403)
                {
                  _key401 = iprot.readString();
                  _val402 = iprot.readString();
                  struct.spec.put(_key401, _val402);
                }
                iprot.readMapEnd();
              }
              struct.setSpecIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // STORAGE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.storage = new CatalogStorage();
              struct.storage.read(iprot);
              struct.setStorageIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PARAMETERS
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map404 = iprot.readMapBegin();
                struct.parameters = new HashMap<String,String>(2*_map404.size);
                String _key405;
                String _val406;
                for (int _i407 = 0; _i407 < _map404.size; ++_i407)
                {
                  _key405 = iprot.readString();
                  _val406 = iprot.readString();
                  struct.parameters.put(_key405, _val406);
                }
                iprot.readMapEnd();
              }
              struct.setParametersIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, CatalogPartitionObject struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.spec != null) {
        oprot.writeFieldBegin(SPEC_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, struct.spec.size()));
          for (Map.Entry<String, String> _iter408 : struct.spec.entrySet())
          {
            oprot.writeString(_iter408.getKey());
            oprot.writeString(_iter408.getValue());
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.storage != null) {
        oprot.writeFieldBegin(STORAGE_FIELD_DESC);
        struct.storage.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.parameters != null) {
        oprot.writeFieldBegin(PARAMETERS_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, struct.parameters.size()));
          for (Map.Entry<String, String> _iter409 : struct.parameters.entrySet())
          {
            oprot.writeString(_iter409.getKey());
            oprot.writeString(_iter409.getValue());
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class CatalogPartitionObjectTupleSchemeFactory implements SchemeFactory {
    public CatalogPartitionObjectTupleScheme getScheme() {
      return new CatalogPartitionObjectTupleScheme();
    }
  }

  private static class CatalogPartitionObjectTupleScheme extends TupleScheme<CatalogPartitionObject> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, CatalogPartitionObject struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      {
        oprot.writeI32(struct.spec.size());
        for (Map.Entry<String, String> _iter410 : struct.spec.entrySet())
        {
          oprot.writeString(_iter410.getKey());
          oprot.writeString(_iter410.getValue());
        }
      }
      struct.storage.write(oprot);
      {
        oprot.writeI32(struct.parameters.size());
        for (Map.Entry<String, String> _iter411 : struct.parameters.entrySet())
        {
          oprot.writeString(_iter411.getKey());
          oprot.writeString(_iter411.getValue());
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, CatalogPartitionObject struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TMap _map412 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, iprot.readI32());
        struct.spec = new HashMap<String,String>(2*_map412.size);
        String _key413;
        String _val414;
        for (int _i415 = 0; _i415 < _map412.size; ++_i415)
        {
          _key413 = iprot.readString();
          _val414 = iprot.readString();
          struct.spec.put(_key413, _val414);
        }
      }
      struct.setSpecIsSet(true);
      struct.storage = new CatalogStorage();
      struct.storage.read(iprot);
      struct.setStorageIsSet(true);
      {
        org.apache.thrift.protocol.TMap _map416 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, iprot.readI32());
        struct.parameters = new HashMap<String,String>(2*_map416.size);
        String _key417;
        String _val418;
        for (int _i419 = 0; _i419 < _map416.size; ++_i419)
        {
          _key417 = iprot.readString();
          _val418 = iprot.readString();
          struct.parameters.put(_key417, _val418);
        }
      }
      struct.setParametersIsSet(true);
    }
  }

}

