/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

#include <iosfwd>

#include <thrift/Thrift.h>
#include <thrift/TApplicationException.h>
#include <thrift/protocol/TProtocol.h>
#include <thrift/transport/TTransport.h>

#include <thrift/cxxfunctional.h>
#include "snappydata_struct_CatalogMetadataRequest.h"

#include <algorithm>
#include <ostream>

#include <thrift/TToString.h>

namespace io { namespace snappydata { namespace thrift {


CatalogMetadataRequest::~CatalogMetadataRequest() noexcept {
}


void CatalogMetadataRequest::__set_schemaName(const std::string& val) {
  this->schemaName = val;
__isset.schemaName = true;
}

void CatalogMetadataRequest::__set_nameOrPattern(const std::string& val) {
  this->nameOrPattern = val;
__isset.nameOrPattern = true;
}

void CatalogMetadataRequest::__set_properties(const std::map<std::string, std::string> & val) {
  this->properties = val;
__isset.properties = true;
}

uint32_t CatalogMetadataRequest::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->schemaName);
          this->__isset.schemaName = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->nameOrPattern);
          this->__isset.nameOrPattern = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->properties.clear();
            uint32_t _size434;
            ::apache::thrift::protocol::TType _ktype435;
            ::apache::thrift::protocol::TType _vtype436;
            xfer += iprot->readMapBegin(_ktype435, _vtype436, _size434);
            uint32_t _i438;
            for (_i438 = 0; _i438 < _size434; ++_i438)
            {
              std::string _key439;
              xfer += iprot->readString(_key439);
              std::string& _val440 = this->properties[_key439];
              xfer += iprot->readString(_val440);
            }
            xfer += iprot->readMapEnd();
          }
          this->__isset.properties = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t CatalogMetadataRequest::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("CatalogMetadataRequest");

  if (this->__isset.schemaName) {
    xfer += oprot->writeFieldBegin("schemaName", ::apache::thrift::protocol::T_STRING, 1);
    xfer += oprot->writeString(this->schemaName);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.nameOrPattern) {
    xfer += oprot->writeFieldBegin("nameOrPattern", ::apache::thrift::protocol::T_STRING, 2);
    xfer += oprot->writeString(this->nameOrPattern);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.properties) {
    xfer += oprot->writeFieldBegin("properties", ::apache::thrift::protocol::T_MAP, 3);
    {
      xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->properties.size()));
      std::map<std::string, std::string> ::const_iterator _iter441;
      for (_iter441 = this->properties.begin(); _iter441 != this->properties.end(); ++_iter441)
      {
        xfer += oprot->writeString(_iter441->first);
        xfer += oprot->writeString(_iter441->second);
      }
      xfer += oprot->writeMapEnd();
    }
    xfer += oprot->writeFieldEnd();
  }
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

void swap(CatalogMetadataRequest &a, CatalogMetadataRequest &b) {
  using ::std::swap;
  swap(a.schemaName, b.schemaName);
  swap(a.nameOrPattern, b.nameOrPattern);
  swap(a.properties, b.properties);
  swap(a.__isset, b.__isset);
}

CatalogMetadataRequest::CatalogMetadataRequest(const CatalogMetadataRequest& other442) {
  schemaName = other442.schemaName;
  nameOrPattern = other442.nameOrPattern;
  properties = other442.properties;
  __isset = other442.__isset;
}
CatalogMetadataRequest::CatalogMetadataRequest( CatalogMetadataRequest&& other443) noexcept {
  schemaName = std::move(other443.schemaName);
  nameOrPattern = std::move(other443.nameOrPattern);
  properties = std::move(other443.properties);
  __isset = std::move(other443.__isset);
}
CatalogMetadataRequest& CatalogMetadataRequest::operator=(const CatalogMetadataRequest& other444) {
  schemaName = other444.schemaName;
  nameOrPattern = other444.nameOrPattern;
  properties = other444.properties;
  __isset = other444.__isset;
  return *this;
}
CatalogMetadataRequest& CatalogMetadataRequest::operator=(CatalogMetadataRequest&& other445) noexcept {
  schemaName = std::move(other445.schemaName);
  nameOrPattern = std::move(other445.nameOrPattern);
  properties = std::move(other445.properties);
  __isset = std::move(other445.__isset);
  return *this;
}
void CatalogMetadataRequest::printTo(std::ostream& out) const {
  using ::apache::thrift::to_string;
  out << "CatalogMetadataRequest(";
  out << "schemaName="; (__isset.schemaName ? (out << to_string(schemaName)) : (out << "<null>"));
  out << ", " << "nameOrPattern="; (__isset.nameOrPattern ? (out << to_string(nameOrPattern)) : (out << "<null>"));
  out << ", " << "properties="; (__isset.properties ? (out << to_string(properties)) : (out << "<null>"));
  out << ")";
}

}}} // namespace