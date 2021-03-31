package com.bb1.tub.api.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.bb1.tub.Identifier;
import com.bb1.tub.Tub;
import com.bb1.tub.api.entites.Damageable;
import com.bb1.tub.api.entites.Player;
import com.bb1.tub.api.interfaces.XPStorable;
import com.bb1.tub.api.loader.Addon;
import com.bb1.tub.api.loader.AddonData;
import com.bb1.tub.api.loader.AddonManager;
import com.bb1.tub.api.storage.StorageContainer;
import com.bb1.tub.api.storage.StorageManager;
import com.bb1.tub.api.world.Location;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

public final class WebManager {
	
	private Server server;
	private ServerConnector serverConnector;
	private ServletHandler servletHandler;
	private int port;
	protected final boolean canBeStarted;
	private final SchemaParser schemaParser = new SchemaParser();
	private final String graphQLSchema = "type Query {\r\n" + 
			"  authToken(token: String): String # Used if the data that is being accessed is restricted\r\n" + 
			"  server: Server!\r\n" + 
			"  players(name: [String!]): [Player!]\r\n" + 
			"  containers(identifier: [String!]): [StorageContainer!]\r\n" + 
			"}\r\n" + 
			"# Holds the data for players\r\n" + 
			"type Player {\r\n" + 
			"  uuid: String! # The players UUID (cannot change)\r\n" + 
			"  name: String! # The players name as a litteraltext (can change)\r\n" + 
			"  whitelisted: Boolean! # If the player is whitelisted (defaults to false)\r\n" + 
			"  banned: Ban! # The players ban data (defaults to this,false,null,null,null)\r\n" + 
			"  location: Location! # The players current location (defaults to 0,0,0,overworld)\r\n" + 
			"  fireTicks: Int! # The amount of ticks a player will be on fire for (defaults to 0)\r\n" + 
			"  health: Health! # The health of the player (defaults to 0/0)\r\n" + 
			"  xp: XP! # The players xp contents (defaults to 0,0)\r\n" + 
			"}\r\n" + 
			"# Holds the data on a players ban status\r\n" + 
			"type Ban {\r\n" + 
			"  bannedPlayer: String! # The player who owns this ban\r\n" + 
			"  banned: Boolean! # If the player is banned (defaults to false)\r\n" + 
			"  banReason: String # The players ban reason\r\n" + 
			"  bannedBy: String # The player who banned the bannedPlayer\r\n" + 
			"  bannedUntil: Int # How long until the player is unbanned (null if permanent)\r\n" + 
			"}\r\n" + 
			"# Holds the players xp data\r\n" + 
			"type XP {\r\n" + 
			"  xp: Int! # (defaults to 0)\r\n" + 
			"  level: Int! # (defaults to 0)\r\n" + 
			"}\r\n" + 
			"type Health {\r\n" + 
			"  health: Float! # (defaults to 0)\r\n" + 
			"  maxHealth: Float! # (defaults to 0)\r\n" + 
			"}\r\n" + 
			"# Holds the location of objects\r\n" + 
			"type Location {\r\n" + 
			"  x: Float! # (defaults to 0)\r\n" + 
			"  y: Float! # (defaults to 0)\r\n" + 
			"  z: Float! # (defaults to 0)\r\n" + 
			"  world: String! # The worlds name (defaults to \"overworld\")\r\n" + 
			"}\r\n" + 
			"# Holds the data on a addon\r\n" + 
			"type Addon {\r\n" + 
			"  name: String! # The addons name\r\n" + 
			"  description: String! # The description of the addon (defaults to \"no description\")\r\n" + 
			"  authors: [String!] # The names of all the plugins authors\r\n" + 
			"  version: String! # The addons version (defaults to \"1.0\")\r\n" + 
			"}\r\n" + 
			"# Holds the data for the server\r\n" + 
			"type Server {\r\n" + 
			"  onlinePlayers: [Player!] # An array of all online players\r\n" + 
			"  addons: [Addon!] # An array of all addons\r\n" + 
			"  version: Float! # The current server version (defaults to \"1.0\")\r\n" + 
			"}\r\n" + 
			"# Holds the data of a storage container\r\n" + 
			"type StorageContainer {\r\n" + 
			"  identifier: Identifier! # The storagecontainers identifier\r\n" + 
			"  entries: [Entry!] # The storagecontainers contents represented as entrys\r\n" + 
			"  class: String! # The class of the storagecontainer\r\n" + 
			"}\r\n" + 
			"# Used to store key and value pairs\r\n" + 
			"type Entry {\r\n" + 
			"  key: String!\r\n" + 
			"  value: String!\r\n" + 
			"}\r\n" + 
			"# Represents an identifier\r\n" + 
			"type Identifier {\r\n" + 
			"  name: String! # Owner of identifier\r\n" + 
			"  key: String! # Whats being identified\r\n" + 
			"  id: String! # The result of the concat: name+':'+key\r\n" + 
			"}";
	
	public WebManager(int port, boolean canBeStarted) {
		this.port = port;
		this.canBeStarted = canBeStarted;
		if (canBeStarted) {
			TypeDefinitionRegistry tdr = schemaParser.parse(graphQLSchema);
			RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
			.type("Query", builder -> {
				builder.dataFetcher("server", new DataFetcher<Object>() {

					@Override
					public Object get(DataFetchingEnvironment env) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("version", (float) Tub.getVersion());
						List<Map<String, Object>> onlinePlayers = new ArrayList<Map<String, Object>>();
						for (Player p : Tub.getAllOnlinePlayers()) {
							onlinePlayers.add(getPlayer(p));
						}
						map.put("onlinePlayers", onlinePlayers);
						List<Map<String, Object>> addons = new ArrayList<Map<String, Object>>();
						for (Addon addon : Tub.getAddonManager().getAddons()) {
							addons.add(getAddon(addon.getData()));
						}
						map.put("addons", addons);
						return map;
					}
					
				});
				builder.dataFetcher("players", new DataFetcher<Object>() {

					@Override
					public Object get(DataFetchingEnvironment environment) throws Exception { // TODO: make work with offline
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						Object str = environment.getArgument("name");
						if (str==null) return list;
						List<?> obj = (List<?>) str;
						for (Object s : obj) {
							if (s!=null && s instanceof String) {
								list.add(getPlayer(Tub.getOnlinePlayer((String)s)));
							}
						}
						return list;
					}
					
				});
				builder.dataFetcher("addons", new DataFetcher<Object>() {

					@Override
					public Object get(DataFetchingEnvironment environment) throws Exception {
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						Object str = environment.getArgument("name");
						if (str==null) return list;
						List<?> obj = (List<?>) str;
						final AddonManager addonManager = Tub.getAddonManager();
						for (Object s : obj) {
							if (s!=null && s instanceof String) {
								list.add(getAddon(addonManager.getAddon((String)s).getData()));
							}
						}
						return list;
					}
					
				});
				builder.dataFetcher("containers", new DataFetcher<Object>() {

					@Override
					public Object get(DataFetchingEnvironment environment) throws Exception {
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						Object str = environment.getArgument("identifier");
						if (str==null) return list;
						List<?> obj = (List<?>) str;
						final StorageManager storageManager = Tub.getStorageManager();
						for (Object s : obj) {
							if (s!=null && s instanceof String) {
								list.add(getStorageContainer(storageManager.getStorageContainer(new Identifier((String)s))));
							}
						}
						return list;
					}
					
				});
				builder.dataFetcher("authToken", new StaticDataFetcher("false")); // TODO
				return builder;
			})
			.build();
	        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(tdr, runtimeWiring);
	        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
			this.server = new Server();
			this.serverConnector = new ServerConnector(this.server);
			this.serverConnector.setPort(this.port);
			this.server.setConnectors(new Connector[] {serverConnector});
			this.servletHandler = new ServletHandler();
			HttpServlet httpServlet = new HttpServlet() {
				
				private static final long serialVersionUID = -5273044129750253597L;
				
				@Override
				protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
					res.setContentType("application/json");
					String query = req.getParameter("query");
					String response;
					int responseCode;
					try {
						response = build.execute(query).getData().toString();
						responseCode = HttpServletResponse.SC_OK;
					} catch (Exception e) {
						response = "{\"status\":\"failed\"}";
						responseCode = HttpServletResponse.SC_BAD_REQUEST;
					}
					res.setStatus(responseCode);
					res.getWriter().println(response);
				}
				
			};
			this.servletHandler.addServletWithMapping(new ServletHolder(httpServlet), "/graphql");
			this.server.setHandler(this.servletHandler);
			try {
				this.server.start();
			} catch (Exception e) {
				System.err.println("[Tub] Failed to load WebManager");
			}
		}
	}
	
	public void stop() {
		try {
			this.serverConnector.stop();
			this.server.stop();
		} catch (Exception e) {
			stop(); // loop until stopped
		}
	}
	
	protected static String token = Tub.getWebDefaults().getOrDefault("authToken", "");
	protected static boolean canTokenBeModified = Boolean.parseBoolean(Tub.getWebDefaults().getOrDefault("canTokenBeChangedWithPutRequest", Boolean.toString(false)));
	
	// This is temporary to make things simpler to read
	
	private Map<String, Object> getPlayer(Player player) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (player==null) return map;
		map.put("uuid", player.getUUID().toString());
		map.put("name", player.getName().toLitteralText().toString());
		map.put("whitelisted", player.isWhitelisted());
		map.put("ban", getBan(player));
		map.put("location", getLocation(player.getLocation()));
		map.put("fireTicks", player.getFireTicks());
		map.put("health", getHealth(player));
		map.put("xp", getXP(player));
		return map;
		
	}
	
	private Map<String, Object> getXP(XPStorable xp) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xp", xp.getXP());
		map.put("level", xp.getXPLevel());
		return map;
	}
	
	private Map<String, Object> getHealth(Damageable d) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("health", (float) d.getHealth());
		map.put("maxHealth", (float) d.getMaxHealth());
		return map;
	}
	
	private Map<String, Object> getBan(Player player) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bannedPlayer", player.getName().toLitteralText().toString());
		map.put("banned", player.isBanned());
		map.put("banReason", "TODO"); // TODO
		map.put("bannedBy", "BradBot_1"); // TODO
		map.put("bannedUntil", Integer.toString(69)); // TODO
		return map;
	}
	
	private Map<String, Object> getLocation(Location location) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (location==null) {
			map.put("x", 0f);
			map.put("y", 0f);
			map.put("z", 0f);
			map.put("world", "overworld");
		} else {
			map.put("x", (float) location.getX());
			map.put("y", (float) location.getY());
			map.put("z", (float) location.getZ());
			map.put("world", location.getWorld().getWorldName());
		}
		return map;
		
	}
	
	private Map<String, Object> getStorageContainer(StorageContainer storageContainer) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("identifier", getIdentifier(storageContainer.getIdentifier()));
		map.put("class", storageContainer.getClass().getName());
		List<Map<String, Object>> entries = new ArrayList<Map<String, Object>>();
		if (!storageContainer.getGraphQLPreference()) {
			map.put("entries", entries);
			return map;
		}
		for (String key : storageContainer.getKeys()) {
			entries.add(getEntry(key, storageContainer.get(key)));
		}
		map.put("entries", entries);
		return map;
	}
	
	private Map<String, Object> getEntry(String key, String value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", key);
		map.put("value", value);
		return map;
	}
	
	private Map<String, Object> getIdentifier(Identifier identifier) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", identifier.getName());
		map.put("key", identifier.getKey());
		map.put("id", identifier.toString());
		return map;
	}
	
	private Map<String, Object> getAddon(AddonData addonData) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", addonData.getName());
		map.put("description", addonData.getDescription());
		map.put("authors", Arrays.asList(addonData.getAuthors()));
		map.put("version", addonData.getVersion());
		return map;
	}
	
}
