'''
Created on Feb 9, 2015

@author: samynpd
'''

import endpoints
import protorpc
from models import Company#, Note, Interview, LineLength, Job, Major
import main
from models import LineLength

WEB_CLIENT_ID = "226229190503-97pgt09qc8tr0g368pa6fpf0kjsof3pm.apps.googleusercontent.com"
ANDROID_CLIENT_ID = "226229190503-7b5arfm067fj9gb8sv3mabto3ge9ldbc.apps.googleusercontent.com"

@endpoints.api(name="careerfair", version="v2", description="Career Fair API",
               audiences=[WEB_CLIENT_ID],
               allowed_client_ids=[endpoints.API_EXPLORER_CLIENT_ID, WEB_CLIENT_ID, ANDROID_CLIENT_ID])
class CareerFairApi(protorpc.remote.Service):
    
    @Company.method(path="company/insert", name="company.insert", http_method="POST")
    def company_insert(self, request):
        
        if request.from_datastore:
            my_company = request
        else:
            my_company = Company(parent=main.PARENT_KEY, name=request.name, bio=request.bio, logo=request.logo)
            
        my_company.put()
        return my_company
    
    @Company.query_method(name="company.list", path="company/list", http_method="GET", 
                          query_fields=("limit", "order", "pageToken"))
    def company_list(self, query):
        return query
    
    @Company.method(name="company.delete", path="company/delete/{entityKey}", 
                    http_method="DELETE", request_fields=("entityKey",))
    
    def company_delete(self, request):
        if not request.from_datastore:
            raise endpoints.NotFoundException("Item to delete not found")
        request.key.delete()
        return Company(name="deleted")
    
    @LineLength.method(path="linelength/insert", name="linelength.insert", http_method="POST")
    def linelength_insert(self, request):
        if request.from_datastore:
            my_linelength = request
        else:
            my_linelength = LineLength(parent=main.PARENT_KEY, length=request.length, company_entity_key=request.company_entity_key)
        
        my_linelength.put()
        return my_linelength
    
    @LineLength.query_method(name="linelength.list", path="linelength/list", http_method="GET", 
                          query_fields=("limit", "order", "pageToken"))
    def linelength_list(self, query):
        return query
    
    @LineLength.method(name="linelength.delete", path="linelength/delete/{entityKey}", 
                    http_method="DELETE", request_fields=("entityKey",))
    
    def linelength_delete(self, request):
        if not request.from_datastore:
            raise endpoints.NotFoundException("Item to delete not found")
        request.key.delete()
        return LineLength(name="deleted")
    
    @LineLength.method(name="linelength.status", path="linelength/status/{company_entity_key}", 
                    http_method="GET", request_fields=("company_entity_key",))
    
    def linelength_status(self, request):
        query = LineLength.query().filter(LineLength.company_entity_key == request.company_entity_key).order(-LineLength.last_touch_date_time)
        
        count = 0
        sumLength = 0
        for linelength in query:
            
            sumLength += linelength.length
            count += 1
            if count > 2:
                break
        
        if count < 1:
            return LineLength(length=0)
        
        length = int(round(sumLength/count))
        return LineLength(length=length)

app = endpoints.api_server([CareerFairApi], restricted=False)