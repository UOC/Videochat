<%-- 
    Document   : searchMeeting
    Created on : 24-mar-2014, 17:47:41
    Author     : Francesc Fernandez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
       
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <!-- Optional theme -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="css/general.css">
    <title>Search session</title>
    </head>
    <body>
    <header id="search"><div class="container"><img src="css/images/logo_invers.png" alt="videochat"/></div></header>
    <div class="container">
        <h3>Player</h3>
        <p><strong>Select a session to play.</strong>Use the following search options to find a session to play.</p>

            <form name="search_meeting_form" action="" method="POST">
                <div class="form-group">
                    <label for="topic" class="control-label">Topic</label>
                    <input type="text" class="form-control" id="topic">
                </div>
                <div class="form-group">
                    <label for="participants" class="control-label">Participants</label>
                    <input type="text" class="form-control" id="participants">
                </div>
                <div class="row">
                    <div class="form-group col-xs-6">
                        <label for="from" class="control-label">From</label>
                        <input type="date" class="form-control" id="from" placeholder="dd/mm/yyyy">
                    </div>
                    <div class="form-group col-xs-6">
                        <label for="to" class="control-label">To</label>
                        <input type="date" class="form-control" id="to" placeholder="dd/mm/yyyy">
                    </div>
                </div>
                <div class="form-group">
                    <label for="room_id" class="control-label">Select room</label>
                    <select name="room_id" class="form-control">
                        <option value="-1">All</option>
                         <c:forEach var="item" items="${listRooms}">
                             <option value="<c:out value="${item.getId()}"/>"><c:out value="${item.getLabel()}"/></option>
                         </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-warning">Search</button>
                </form>
        <hr/>
        <p><strong>Results.</strong>Select the session you are interested to play.</p>
        <div class="table-responsive">
            <table class="table table-striped table-condensed table-hover">
                <thead >
                    <tr>
                        <th class="borderW selec col1" scope="col">Topic</th>
                        <th class="borderW mida  col2" scope="col">Participants</th>
                        <th class="borderW modif col3" scope="col">From (date)</th>
                        <th class="borderW modif col4" scope="col">To (date)</th>
                        <th class="borderW modif col5" scope="col">Length</th>
                        <th class="borderW modif col5" scope="col">View</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${listMR}">
                   <tr>
                       <td><c:out value="${item.getTopic()}"/></td>
                       <td><c:forEach items="${item.getParticipants()}" var="participant">
                               ${participant.getPk().getUser().getFullname()},  
                            </c:forEach></td>
                       <td>${item.getStart_meeting_txt()}</td>
                       <td>${item.getEnd_meeting_txt()}</td>
                       <td>${item.getTotal_time_txt()}</td>
                       <td><a href="player.htm?id=${item.getId()}">View</a></td>
                    </tr>
                </c:forEach>  
            </tbody>
        </table>
        </div>
    </div>
</body>
</html>