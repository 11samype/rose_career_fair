'''
Created on Feb 9, 2015

@author: samynpd
'''

import endpoints
import protorpc
from models import Company#, Note, Interview, LineLength, Job, Major
import main

@endpoints.api(name="careerfair", version="v1", description="Career Fair API")
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
    
app = endpoints.api_server([CareerFairApi], restricted=False)