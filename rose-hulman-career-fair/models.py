from google.appengine.ext import ndb

from endpoints_proto_datastore.ndb.model import EndpointsModel

class Company(EndpointsModel):
    _message_fields_schema = ("entityKey", "name", "bio", "logo", "majors", "jobs", "favorite", "table")
    name = ndb.StringProperty()
    bio = ndb.TextProperty()
    logo = ndb.StringProperty()
    majors = ndb.StringProperty(repeated=True)
    jobs = ndb.StringProperty(repeated=True)
    favorite = ndb.BooleanProperty()
    table = ndb.IntegerProperty()
    last_touch_date_time = ndb.DateTimeProperty(auto_now=True)
    
class Note(EndpointsModel):
    _message_fields_schema = ("entityKey", "note", "company_entity_key")
    note = ndb.StringProperty()
    company_entity_key = ndb.KeyProperty(kind=Company)
    last_touch_date_time = ndb.DateTimeProperty(auto_now=True)
    
class LineLength(EndpointsModel):
    _message_fields_schema = ("entityKey", "length", "company_entity_key")
    length = ndb.IntegerProperty()
    company_entity_key = ndb.KeyProperty(kind=Company)
    last_touch_date_time = ndb.DateTimeProperty(auto_now=True)
    
# class Interview(EndpointsModel):
#     _message_fields_schema = ("entityKey", "date_time", "owner", "company_entity_key")
#     date_time = ndb.DateTimeProperty()
#     owner = ndb.UserProperty()
#     company_entity_key = ndb.KeyProperty()
#     last_touch_date_time = ndb.DateTimeProperty(auto_now=True)
    
# class Job(EndpointsModel):
#     _message_fields_schema = ("entityKey", "name", "company_entity_key")
#     name = ndb.StringProperty()
#     company_entity_key = ndb.KeyProperty(kind=Company)
#     last_touch_date_time = ndb.DateTimeProperty(auto_now=True)
#     
# class Major(EndpointsModel):
#     _message_fields_schema = ("entityKey", "name", "company_entity_key")
#     name = ndb.StringProperty()
#     company_entity_key = ndb.KeyProperty(kind=Company)
#     last_touch_date_time = ndb.DateTimeProperty(auto_now=True)
    
class Favorite(EndpointsModel):
    _message_fields_schema = ("entityKey", "company_entity_key", "message")
    company_entity_key = ndb.KeyProperty(kind=Company)
    message = ndb.StringProperty()
    last_touch_date_time = ndb.DateTimeProperty(auto_now=True)