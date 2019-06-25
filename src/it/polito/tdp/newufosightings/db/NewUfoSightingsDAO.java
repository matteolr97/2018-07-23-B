package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;
import it.polito.tdp.newufosightings.model.StateSighting;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<State> loadAllStates(Map<String, State> idMap) {
		String sql = "SELECT * FROM state";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				idMap.put(state.getId(), state);
				result.add(state);
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return result;
	}
	
/*	public List<State> getVicini(Map<String, State> idMap, State s){
		String sql = "SELECT state1 FROM neighbor WHERE state2 =?";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, s.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = idMap.get(rs.getString("state1"));
				if(s== null)
					System.out.println("ERRORE");
				else
				result.add(state);
			}
			
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return result;
	}

	public int getPeso(State s, State stemp, int anno, int xG) {
		String sql = "SELECT  COUNT(s1.id)\n" + 
				"FROM sighting AS s1, sighting AS s2 \n" + 
				"WHERE DATEDIFF(s1.DATETIME,s2.DATETIME)<= ? AND s1.state=? AND s2.state=?\n" + 
				"AND YEAR(s1.DATETIME)= ? AND YEAR(s1.DATETIME)=YEAR(s2.DATETIME)";
		int count = 0;
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, xG);
			st.setString(2, s.getId());
			st.setString(3, stemp.getId());
			st.setInt(4, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				count++;
			}
			
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}		return count;
	}*/
	public List<StateSighting> stateWithSighting(Map<String, State>idMap, int anno,int giorni){
		String sql = "SELECT s1.state AS st1, s2.state AS st2, COUNT(*) AS cnt " + 
				"FROM sighting AS s1, sighting AS  s2, neighbor AS n " + 
				"WHERE s1.state = n.state1 " + 
				"AND s2.state = n.state2 " + 
				"AND s1.state > s2.state " + 
				"AND YEAR(s1.DATETIME) = ? " + 
				"AND YEAR(s2.DATETIME) = ? " + 
				"AND DATEDIFF(s1.DATETIME, s2.DATETIME) < ? " + 
				"GROUP BY st1, st2";
		List<StateSighting> result = new ArrayList<StateSighting>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			st.setInt(3, giorni);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State s1 = idMap.get(rs.getString("st1"));
				State s2 = idMap.get(rs.getString("st2"));
				int peso = rs.getInt("cnt");
				
				if (s1 == null || s2 == null) {
					System.out.println("Errore");
				} else {
					result.add(new StateSighting(s1, s2, peso));
				}
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

}

