'''
Created on Feb 9, 2015

@author: samynpd
'''
import logging
import endpoints
import protorpc
from models import Company, Note, LineLength, Favorite
import main
from endpoints.users_id_token import get_current_user
from google.appengine.ext.key_range import ndb


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
            my_company = Company(parent=main.PARENT_KEY, name=request.name, bio=request.bio, logo=request.logo,
                                 jobs=request.jobs, majors=request.majors, table=request.table, website=request.website)
            
        my_company.put()
        return my_company
    
    @Company.method(user_required=True, path="company/favorite/insert", name="company.favorite.insert", http_method="POST")
    def company_insert_favorite(self, request):
        user = endpoints.get_current_user()
        favBool = request.favorite
        key = ndb.Key(urlsafe=request.entityKey)
        
        query_favorite = Favorite.query(ancestor=main.get_parent_key(user)).filter(Favorite.company_entity_key == key)
        
        my_favorite = Favorite(parent=main.get_parent_key(user), company_entity_key=key)
        
        found = False
        
        logging.info(favBool)
        
        for item in query_favorite:
            logging.info("favorite logged")
            my_favorite = item
            found = True
        
        logging.info(my_favorite)
        
        if favBool:
            
            logging.info("favorite bool = True")
            
            if not found:
                
                logging.info("creating favorite")
#                 my_favorite = Favorite(parent=main.get_parent_key(user), company_entity_key=key)
                my_favorite.put()
        else:
            
            if found:
                logging.info("delete")
                my_favorite.key.delete()
            
        return request
    
    @Company.query_method(name="company.list", path="company/list", http_method="GET", 
                          query_fields=("limit", "order", "pageToken"))
    def company_list(self, query):
        return query
    
    @Company.query_method(user_required=True, name="company.favorite.list", path="company/favorite/list", http_method="GET", 
                          query_fields=("limit", "order", "pageToken"))
    def company_list_favorite(self, query):
        # list of companies with favorite boolean attached to favorites
        user = endpoints.get_current_user()
        
        for company in query:
            company.favorite = False
            
            key = ndb.Key(urlsafe=company.entityKey)
            favQuery = Favorite.query(ancestor=main.get_parent_key(user)).filter(Favorite.company_entity_key == key)
            
            for favorite in favQuery:
                company.favorite = True
           
        return query
    
#     @Company.query_method(user_required=True, name="company.favorite.favorite", path="company/favorite/favorite", http_method="GET", 
#                           query_fields=("limit", "order", "pageToken"))
#     def company_favorite_favorite(self, query):
#         # list of just the users favorite companies
#         user = endpoints.get_current_user()
#         
#         favQuery = Favorite.query(ancestor=main.get_parent_key(user))
#         
#         favKeys = []
#         
#         for fav in favQuery:
# #             favKeys.append(fav.company_entity_key)
#             favKeys.append(str (fav.company_entity_key))
#             logging.info(str (fav.company_entity_key))
#         
# #         list_of_companies = Company.query()
#         
#         
#         
# #         list_of_companies.filter('__Key__ IN', favKeys)
# 
# #         list_of_companies = Company.get_by_id(favKeys)
#         
# #         return list_of_companies
# 
# #         result = ndb.get_multi(favKeys)
# 
#         Company.get
# 
#         result = ndb.get_multi(favKeys)
#         
#         return result
    
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
    
    @Note.query_method(name="note.list", path="note/list", http_method="GET", 
                          query_fields=("limit", "order", "pageToken"))
    def note_list(self, query):
        return query
    
    @Note.method(user_required=True, name="note.company", path="note/company/{company_entity_key}", http_method="GET", 
                       request_fields=("company_entity_key",))
    def note_for_company(self, request):
        user = endpoints.get_current_user()
        query = Note.query(ancestor=main.get_parent_key(user)).filter(Note.company_entity_key == request.company_entity_key)
        
        returnNote = Note(note="", company_entity_key=request.company_entity_key)
        
        for note in query:
            returnNote = note
            break
        
        return returnNote
     
    @Note.method(user_required=True, name="note.insert", path="note/insert", http_method="POST")
     
    def note_insert(self, note):
        if note.from_datastore:
            note_with_parent = note
        else:
            note_with_parent = Note(parent=main.get_parent_key(endpoints.get_current_user()),
                                    note = note.note, company_entity_key = note.company_entity_key)
             
        note_with_parent.put()
        return note_with_parent
    
    @Favorite.query_method(name="favorite.list", path="favorite/list", http_method="GET", 
                          query_fields=("limit", "order", "pageToken"))
    def favorite_list(self, query):
        return query
    
    @Favorite.method(user_required=True, name="favorite.company", path="favorite/company/{company_entity_key}", http_method="GET", 
                       request_fields=("company_entity_key",))
    def favorite_for_company(self, request):
        user = endpoints.get_current_user()
        query = Favorite.query(ancestor=main.get_parent_key(user)).filter(Favorite.company_entity_key == request.company_entity_key)
        
        returnFavorite = Favorite(message="null")
        
        for favorite in query:
            returnFavorite = favorite
            break
        
        return returnFavorite
     
    @Favorite.method(user_required=True, name="favorite.insert", path="favorite/insert", http_method="POST")
     
    def favorite_insert(self, favorite):
        if favorite.from_datastore:
            favorite_with_parent = favorite
        else:
            favorite_with_parent = Favorite(parent=main.get_parent_key(endpoints.get_current_user()),
                                    company_entity_key = favorite.company_entity_key)
             
        favorite_with_parent.put()
        return favorite_with_parent
    
    @Favorite.method(user_required=True, name="favorite.delete", path="favorite/delete/{entityKey}", 
                    http_method="DELETE", request_fields=("entityKey",))
    
    def favorite_delete(self, request):
        if not request.from_datastore:
            raise endpoints.NotFoundException("Item to delete not found")
        request.key.delete()
        return Favorite(message="deleted")
    
app = endpoints.api_server([CareerFairApi], restricted=False)