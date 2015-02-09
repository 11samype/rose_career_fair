from google.appengine.ext import ndb

from endpoints_proto_datastore.ndb.model import EndpointsModel

class Company(EndpointsModel):
    _message_fields_schema = ("entityKey", "name", "bio", "logo")
    name = ndb.StringProperty()
    bio = ndb.StringProperty()
    logo = ndb.StringProperty()
    last_touch_date_time = ndb.DateTimeProperty(auto_now=True)
    
class Note(EndpointsModel):
    _message_fields_schema = ("entityKey", "note", "owner", "company_entity_key")
    note = ndb.StringProperty()
    owner = ndb.UserProperty()
    company_entity_key = ndb.IntegerProperty()
    last_touch_date_time = ndb.DateTimeProperty(auto_now=True)
    
class LineLength(EndpointsModel):
    _message_fields_schema = ("entityKey", "length", "company_entity_key")
    length = ndb.IntegerProperty()
    company_entity_key = ndb.IntegerProperty()
    
class Interview(EndpointsModel):
    _message_fields_schema = ("entityKey", "date_time", "owner", "company_entity_key")
    date_time = ndb.DateTimeProperty()
    owner = ndb.UserProperty()
    company_entity_key = ndb.IntegerProperty()
    last_touch_date_time = ndb.DateTimeProperty(auto_now=True)
    
class Job(EndpointsModel):
    _message_fields_schema = ("entityKey", "name", "company_entity_key")
    name = ndb.StringProperty()
    company_entity_key = ndb.IntegerProperty()
    last_touch_date_time = ndb.DateTimeProperty(auto_now=True)
    
class Major(EndpointsModel):
    _message_fields_schema = ("entityKey", "name", "company_entity_key")
    name = ndb.StringProperty()
    company_entity_key = ndb.IntegerProperty()
    last_touch_date_time = ndb.DateTimeProperty(auto_now=True)