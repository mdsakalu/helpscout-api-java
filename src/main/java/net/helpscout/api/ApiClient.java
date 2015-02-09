package net.helpscout.api;

import com.google.gson.*;
import net.helpscout.api.adapters.*;
import net.helpscout.api.cbo.*;
import net.helpscout.api.exception.*;
import net.helpscout.api.model.*;
import net.helpscout.api.model.Customer;
import net.helpscout.api.model.customer.SearchCustomer;
import net.helpscout.api.model.thread.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class ApiClient {

	private final static String BASE_URL = "https://api.helpscout.net/v1/";
	private final static String METHOD_GET = "GET";
	private final static String METHOD_POST = "POST";
	private final static String METHOD_PUT = "PUT";
	private final static String METHOD_DELETE = "DELETE";

    private final static int HTTP_STATUS_OK = 200;
    private final static int HTTP_STATUS_CREATED = 201;

	private String apiKey = "";

	private static ApiClient instance = null;

	private ApiClient() {
	}
	
	private static class ApiClientHolder {
		private static final ApiClient INSTANCE = new ApiClient();
	}

	public static ApiClient getInstance() {
		return ApiClientHolder.INSTANCE;
	}

	public void setKey(String apiKey) {
		this.apiKey = apiKey;
	}

    /**
     * Gets the mailbox with the specified id.
     *
     * @param mailboxID
     * @return Mailbox
     * @throws ApiException
     */
	public Mailbox getMailbox(Integer mailboxID) throws ApiException {
		return (Mailbox)getItem("mailboxes/" + mailboxID + ".json", Mailbox.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the mailbox with the specified id.
     *
     * @param mailboxID
     * @param fields
     * @return Mailbox
     * @throws ApiException
     */
	public Mailbox getMailbox(Integer mailboxID, List<String> fields) throws ApiException {
		if (mailboxID == null || mailboxID < 1) {
			throw new ApiException("Invalid mailboxId in getMailbox");
		}
		String url = setFields("mailboxes/" + mailboxID + ".json", fields);
		return (Mailbox)getItem(url, Mailbox.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the first page of mailboxes.
     *
     * @return Page
     * @throws ApiException
     */
	public Page getMailboxes() throws ApiException {
		return getPage("mailboxes.json", Mailbox.class, HTTP_STATUS_OK);
	}

    /**
     * Get the first page of mailboxes.
     *
     * @param fields
     * @return Page
     * @throws ApiException
     */
	public Page getMailboxes(List<String> fields) throws ApiException {
		String url = setFields("mailboxes.json", fields);
		return getPage(url, Mailbox.class, HTTP_STATUS_OK);
	}

    /**
     * Gets a list of mailboxes.
     *
     * @param queryParams
     * @return Page
     * @throws ApiException
     */
    public Page getMailboxes(Map<String, String> queryParams) throws ApiException {
        return getPage("mailboxes.json", queryParams, Mailbox.class, HTTP_STATUS_OK);
    }

    /**
     * Gets the first page of folders for the specified mailbox.
     *
     * @param mailboxId
     * @return Page
     * @throws ApiException
     */
	public Page getFolders(Integer mailboxId) throws ApiException {
		return getPage("mailboxes/" + mailboxId + "/folders.json", Folder.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the first page of folders for the specified mailbox.
     *
     * @param mailboxId
     * @param fields
     * @return Page
     * @throws ApiException
     */
	public Page getFolders(Integer mailboxId, List<String> fields) throws ApiException {
		String url = setFields("mailboxes/" + mailboxId + "/folders.json", fields);
		return getPage(url, Folder.class, HTTP_STATUS_OK);
	}

    /**
     * Gets a page of folders for the specified mailbox.
     *
     * @param mailboxId
     * @param queryParams
     * @return Page
     * @throws ApiException
     */
    public Page getFolders(Integer mailboxId, Map<String, String> queryParams) throws ApiException {
        return getPage("mailboxes/" + mailboxId + "/folders.json", queryParams, Folder.class, HTTP_STATUS_OK);
    }

    /**
     * Gets the first page of conversations for the specified mailbox and folder.
     *
     * @param mailboxID
     * @param folderID
     * @return Page
     * @throws ApiException
     */
	public Page getConversationsForFolder(Integer mailboxID, Integer folderID) throws ApiException {
		return getPage("mailboxes/" + mailboxID + "/folders/" + folderID + "/conversations.json", Conversation.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the first page of conversations for the specified mailbox and folder.
     *
     * @param mailboxID
     * @param folderID
     * @param fields
     * @return Page
     * @throws ApiException
     */
	public Page getConversationsForFolder(Integer mailboxID, Integer folderID, List<String> fields) throws ApiException {
		String url = setFields("mailboxes/" + mailboxID + "/folders/" + folderID + "/conversations.json", fields);
		return getPage(url, Conversation.class, HTTP_STATUS_OK);
	}

    /**
     * Gets a page of conversations for the specified mailbox and folder.
     *
     * @param mailboxID
     * @param folderID
     * @param queryParams
     * @return Page
     * @throws ApiException
     */
    public Page getConversationsForFolder(Integer mailboxID, Integer folderID, Map<String, String> queryParams) throws ApiException {
        return getPage("mailboxes/" + mailboxID + "/folders/" + folderID + "/conversations.json", queryParams, Conversation.class, HTTP_STATUS_OK);
    }

    /**
     * Gets the first page of conversations for the specified mailbox.
     *
     * @param mailboxID
     * @return Page
     * @throws ApiException
     */
	public Page getConversationsForMailbox(Integer mailboxID) throws ApiException {
		return getPage("mailboxes/" + mailboxID + "/conversations.json", Conversation.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the first page of conversations for the specified mailbox.
     *
     * @param mailboxID
     * @param fields
     * @return Page
     * @throws ApiException
     */
	public Page getConversationsForMailbox(Integer mailboxID, List<String> fields) throws ApiException {
		String url = setFields("mailboxes/" + mailboxID + "/conversations.json", fields);
		return getPage(url, Conversation.class, HTTP_STATUS_OK);
	}

    /**
     * Gets a page of conversations for the specified mailbox.
     *
     * @param mailboxID
     * @param queryParams
     * @return Page
     * @throws ApiException
     */
    public Page getConversationsForMailbox(Integer mailboxID, Map<String, String> queryParams) throws ApiException {
        return getPage("mailboxes/" + mailboxID + "/conversations.json", queryParams, Conversation.class, HTTP_STATUS_OK);
    }

    /**
     * Gets the first page of conversations for the specified mailbox and customer.
     *
     * @param mailboxID
     * @param customerID
     * @return Page
     * @throws ApiException
     */
	public Page getConversationsForCustomerByMailbox(Integer mailboxID, Integer customerID) throws ApiException {
		return getPage("mailboxes/" + mailboxID + "/customers/" + customerID + "/conversations.json", Conversation.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the first page of conversations for the specified mailbox and customer.
     *
     * @param mailboxID
     * @param customerID
     * @param fields
     * @return Page
     * @throws ApiException
     */
	public Page getConversationsForCustomerByMailbox(Integer mailboxID, Integer customerID, List<String> fields) throws ApiException {
		String url = setFields("mailboxes/" + mailboxID + "/customers/" + customerID + "/conversations.json", fields);
		return getPage(url, Conversation.class, HTTP_STATUS_OK);
	}

    /**
     * Gets a page of conversations for the specified mailbox and customer.
     *
     * @param mailboxID
     * @param customerID
     * @param queryParams
     * @return Page
     * @throws ApiException
     */
    public Page getConversationsForCustomerByMailbox(Integer mailboxID, Integer customerID, Map<String, String> queryParams) throws ApiException {
        return getPage("mailboxes/" + mailboxID + "/customers/" + customerID + "/conversations.json", queryParams, Conversation.class, HTTP_STATUS_OK);
    }

    /**
     * Gets the conversation with the specified id.
     *
     * @param conversationID
     * @return Conversation
     * @throws ApiException
     */
	public Conversation getConversation(Long conversationID) throws ApiException {
		return (Conversation)getItem("conversations/" + conversationID + ".json", Conversation.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the conversation with the specified id.
     *
     * @param conversationID
     * @param fields
     * @return Conversation
     * @throws ApiException
     */
	public Conversation getConversation(Integer conversationID, List<String> fields) throws ApiException {
		if (conversationID == null || conversationID < 1) {
			throw new ApiException("Invalid conversationId in getConversation");
		}
		String url = setFields("conversations/" + conversationID + ".json", fields);
		return (Conversation)getItem(url, Conversation.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the thread source for the specified conversation and thread.
     *
     * @param conversationID
     * @param threadID
     * @return String
     * @throws ApiException
     */
	public String getThreadSource(Integer conversationID, Integer threadID) throws ApiException {
		if (conversationID == null || conversationID < 1) {
			throw new ApiException("Invalid conversationID in getThreadSource");
		}
		if (threadID == null || threadID < 1) {
			throw new ApiException("Invalid threadID in getThreadSource");
		}
		String json;
		try {
			json = doGet("conversations/" + conversationID + "/thread-source/" + threadID + ".json", HTTP_STATUS_OK);
		} catch(RuntimeException e) {
			if (e.getCause() instanceof NotFoundException) {
				json = null;
			} else {
				throw e;
			}
		}
		if (json != null) {
			JsonElement obj = (new JsonParser()).parse(json);
			JsonElement elem  = obj.getAsJsonObject().get("item");
			return getDecoded(elem.getAsJsonObject().get("data").getAsString());
		} 
		return null;
	}

    /**
     * Gets the attachment data for the specified attachment id.
     *
     * @param attachmentID
     * @return String
     * @throws ApiException
     */
	public String getAttachmentData(Integer attachmentID) throws ApiException {
		if (attachmentID == null || attachmentID < 1) {
			throw new ApiException("Invalid attachmentID in getAttachmentData");
		}
		String json = doGet("attachments/" + attachmentID + "/data.json", HTTP_STATUS_OK);
		JsonElement obj = (new JsonParser()).parse(json);
		JsonElement elem  = obj.getAsJsonObject().get("item");
		return getDecoded(elem.getAsJsonObject().get("data").getAsString());
	}

    /**
     * Gets a page of all tags.
     *
     * @param queryParams
     * @return Page
     * @throws ApiException
     */
    public Page getTags(Map<String,String> queryParams) throws ApiException {
        return getPage("tags.json", queryParams, Tag.class, HTTP_STATUS_OK);
    }

    /**
     * Gets the first page of customers.
     *
     * @return Page
     * @throws ApiException
     */
	public Page getCustomers() throws ApiException {
		return getCustomers(new Integer(null));
	}

    /**
     * Gets a page of customers.
     *
     * @param page
     * @return Page
     * @throws ApiException
     */
	public Page getCustomers(Integer page) throws ApiException {
		if (page != null) {
			return getPage("customers.json?page=" + page, Customer.class, HTTP_STATUS_OK);
		} else {
			return getPage("customers.json", Customer.class, HTTP_STATUS_OK);
		}
	}

    /**
     * Gets the first page of customers.
     *
     * @param fields
     * @return Page
     * @throws ApiException
     */
	public Page getCustomers(List<String> fields) throws ApiException {
		String url = setFields("customers.json", fields);
		return getPage(url, Customer.class, HTTP_STATUS_OK);
	}

    /**
     * Gets a page of customers.
     *
     * @param mailboxId
     * @param page
     * @param fields
     * @return Page
     * @throws ApiException
     */
	public Page getCustomersForMailbox(Long mailboxId, Integer page, List<String> fields) throws ApiException {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append("mailboxes/").append(mailboxId).append("/customers.json");
		if (page != null) {
			sbUrl.append("?page=").append(page);
		}
		String url = setFields(sbUrl.toString(), fields);
		return getPage(url, Customer.class, HTTP_STATUS_OK);
	}

    /**
     * Searches for the first page of customers using any combination of email, firstName, lastName.
     *
     * @param email
     * @param firstName
     * @param lastName
     * @return Page
     * @throws ApiException
     */
	public Page searchCustomers(String email, String firstName, String lastName) throws ApiException {
		return searchCustomers(email, firstName, lastName, null, null);
	}

    /**
     * Returns a page of customers by searching on any combination of email, firstName, lastName.
     *
     * @param email
     * @param firstName
     * @param lastName
     * @param page
     * @param fields
     * @return Page
     * @throws ApiException
     */
	public Page searchCustomers(String email, String firstName, String lastName, Integer page,
			List<String> fields) throws ApiException {
        Map<String,String> params = getCustomerSearchParams(email, firstName, lastName, page);
        String url = setFields("customers.json", fields);
		return getPage(url, params, Customer.class, HTTP_STATUS_OK);
	}

    /**
     * Returns a page of SearchConversation objects by searching using the query.
     *
     * @param query
     * @param sortField
     * @param sortOrder
     * @param page
     * @return Page
     * @throws ApiException
     */
    public Page searchConversations(String query, String sortField, String sortOrder, Integer page) throws ApiException {
        Map<String,String> params = new HashMap<String,String>();
        params.put("query", query);
        if (sortField != null && sortField.trim().length() > 0) {
            params.put("sortField", sortField);
        }
        if (sortOrder != null && sortOrder.trim().length() > 0) {
            params.put("sortOrder", sortOrder);
        }
        params.put("page", String.valueOf(page));
        return getPage("search/conversations.json", params, SearchConversation.class, HTTP_STATUS_OK);
    }

    /**
     * Returns a page of SearchCustomer objects by searching using the query.
     *
     * @param query
     * @param sortField
     * @param sortOrder
     * @param page
     * @return Page
     * @throws ApiException
     */
    public Page searchCustomers(String query, String sortField, String sortOrder, Integer page) throws ApiException {
        Map<String,String> params = new HashMap<String,String>();
        params.put("query", query);
        if (sortField != null && sortField.trim().length() > 0) {
            params.put("sortField", sortField);
        }
        if (sortOrder != null && sortOrder.trim().length() > 0) {
            params.put("sortOrder", sortOrder);
        }
        params.put("page", String.valueOf(page));
        return getPage("search/customers.json", params, SearchCustomer.class, HTTP_STATUS_OK);
    }

    /**
     * Gets the customer with the specified id.
     *
     * @param customerId
     * @return Customer
     * @throws ApiException
     */
	public Customer getCustomer(Long customerId) throws ApiException {
		return (Customer)getItem("customers/" + customerId + ".json", Customer.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the customer with the specified id.
     *
     * @param customerId
     * @param fields
     * @return Customer
     * @throws ApiException
     */
	public Customer getCustomer(Long customerId, List<String> fields) throws ApiException {
		if (customerId == null || customerId < 1) {
			throw new ApiException("Invalid customerId in getCustomer");
		}
		String url = setFields("customers/" + customerId + ".json", fields);
		return (Customer)getItem(url, Customer.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the user associated with the API key used to make the request.
     *
     * @return User
     * @throws ApiException
     */
    public User getUserMe() throws ApiException {
        return (User)getItem("users/me.json", User.class, HTTP_STATUS_OK);
    }

    /**
     * Gets the user with the specified id.
     *
     * @param userID
     * @return User
     * @throws ApiException
     */
	public User getUser(Integer userID) throws ApiException {
		return (User)getItem("users/" + userID + ".json", User.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the user with the specified id.
     *
     * @param userID
     * @param fields
     * @return User
     * @throws ApiException
     */
	public User getUser(Integer userID, List<String> fields) throws ApiException {
		if (userID == null || userID < 1) {
			throw new ApiException("Invalid userId in getUser");
		}
		String url = setFields("users/" + userID + ".json", fields);
		return (User)getItem(url, User.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the first page of users.
     *
     * @return Page
     * @throws ApiException
     */
	public Page getUsers() throws ApiException {
		return getPage("users.json", User.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the first page of users.
     *
     * @param fields
     * @return Page
     * @throws ApiException
     */
	public Page getUsers(List<String> fields) throws ApiException {
		String url = setFields("users.json", fields);
		return getPage(url, User.class, HTTP_STATUS_OK);
	}

    /**
     * Gets a page of users.
     *
     * @param queryParams
     * @return Page
     * @throws ApiException
     */
    public Page getUsers(Map<String, String> queryParams) throws ApiException {
        return getPage("/users.json", queryParams, User.class, HTTP_STATUS_OK);
    }

    /**
     * Gets the first page of users for the specified mailbox.
     *
     * @param mailboxId
     * @return Page
     * @throws ApiException
     */
	public Page getUsersForMailbox(Integer mailboxId) throws ApiException {
		return getPage("mailboxes/" + mailboxId + "/users.json", User.class, HTTP_STATUS_OK);
	}

    /**
     * Gets the first page of users for the specified mailbox.
     *
     * @param mailboxId
     * @param fields
     * @return Page
     * @throws ApiException
     */
	public Page getUsersForMailbox(Integer mailboxId, List<String> fields) throws ApiException {
		String url = setFields("mailboxes/" + mailboxId + "/users.json", fields);
		return getPage(url, User.class, HTTP_STATUS_OK);
	}

    /**
     * Gets a page of users for the specified mailbox.
     *
     * @param mailboxId
     * @param queryParams
     * @return Page
     * @throws ApiException
     */
    public Page getUsersForMailbox(Integer mailboxId, Map<String, String> queryParams) throws ApiException {
        return getPage("mailboxes/" + mailboxId + "/users.json", queryParams, User.class, HTTP_STATUS_OK);
    }

    /**
     * Creates a new customer.
     *
     * @param customer
     * @throws ApiException
     */
	public void createCustomer(Customer customer) throws ApiException {
		String json = new Gson().toJson(customer);
		Long id = (Long)doPost("customers.json", json, HTTP_STATUS_CREATED);
		customer.setId(id);
	}

    /**
     * Updates an existing customer.
     *
     * @param customer
     * @throws ApiException
     */
	public void updateCustomer(Customer customer) throws ApiException {
		GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String json = builder.create().toJson(customer, Customer.class);
		doPut("customers/" + customer.getId() + ".json", json, HTTP_STATUS_OK);
	}

    /**
     * Creates a new conversations.
     *
     * @param conversation
     * @throws ApiException
     */
	public void createConversation(Conversation conversation) throws ApiException {
		createConversation(conversation, false);
	}

    /**
     * Creates a new conversation.
     *
     * @param conversation
     * @param imported
     * @throws ApiException
     */
	public void createConversation(Conversation conversation, boolean imported) throws ApiException {
		GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
				.registerTypeAdapter(ThreadState.class, new ThreadStateAdapter())
				.registerTypeAdapter(Status.class, new StatusAdapter())
				.registerTypeAdapter(PersonType.class, new PersonTypeAdapter())
				.registerTypeAdapter(ThreadType.class, new ThreadTypeAdapter())
				.registerTypeAdapter(ConversationType.class, new ConversationTypeAdapter());

		String json = builder.create().toJson(conversation);

		StringBuilder url = new StringBuilder("conversations.json");
		if (imported) {
			url.append("?imported=true");
		}

		Long id = (Long)doPost(url.toString(), json, HTTP_STATUS_CREATED);
		conversation.setId(id);
	}

    /**
     * Creates a new thread on the specified conversation.
     *
     * @param conversationId
     * @param thread
     * @throws ApiException
     */
	public void createConversationThread(Long conversationId, ConversationThread thread) throws ApiException {
		createConversationThread(conversationId, thread, false);
	}

    /**
     * Creates a new thread on the specified conversation.
     *
     * @param conversationId
     * @param thread
     * @param imported
     * @throws ApiException
     */
	public void createConversationThread(Long conversationId, ConversationThread thread, boolean imported) throws ApiException {
		try {
			setThreadProperties(thread);

			GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
					.registerTypeAdapter(ThreadState.class, new ThreadStateAdapter())
					.registerTypeAdapter(Status.class, new StatusAdapter())
					.registerTypeAdapter(PersonType.class, new PersonTypeAdapter());

			String json = builder.create().toJson(thread);

			StringBuilder url = new StringBuilder("conversations/").append(conversationId).append(".json");
			if (imported) {
				url.append("?imported=true");
			}

			doPost(url.toString(), json, HTTP_STATUS_CREATED);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ApiException(ex.getMessage());
		}
	}

    /**
     * Update the body text of the specified thread.
     *
     * @param conversationId
     * @param threadId
     * @param text
     * @return
     * @throws ApiException
     */
    public void updateConversationThreadText(Long conversationId, Long threadId, String text) throws ApiException {
        Map<String,String> threadBody = new HashMap<String, String>();
        threadBody.put("body", text);

        GsonBuilder builder = new GsonBuilder();
        String json = builder.create().toJson(threadBody);

        doPut("conversations/" + conversationId + "/threads/" + threadId +  ".json", json, HTTP_STATUS_OK);
    }

    /**
     * Deletes the specified conversation.
     *
     * @param id
     * @throws ApiException
     */
	public void deleteConversation(Long id) throws ApiException {
		String url = "conversations/" + id + ".json";
		doDelete(url, HTTP_STATUS_OK);
	}

    /**
     * Updates the specified conversation.
     *
     * @param conversation
     * @throws ApiException
     */
	public void updateConversation(Conversation conversation) throws ApiException {
		GsonBuilder builder = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
				.registerTypeAdapter(ThreadState.class, new ThreadStateAdapter())
				.registerTypeAdapter(Status.class, new StatusAdapter());

		String json = builder.create().toJson(conversation, Conversation.class);
		doPut("conversations/" + conversation.getId() + ".json", json, HTTP_STATUS_OK);
	}

	/**
	 * Uploads an attachment to Help Scout and assigns a hash value to the attachment.
	 * Once the hash has been set for an attachment, the attachment can be included
	 * when creating a new thread.
	 *
	 * @param attachment the attachment to be created
	 * @throws ApiException
	 */
	public void createAttachment(Attachment attachment) throws ApiException {
		String json = new Gson().toJson(attachment);
		String hash = (String)doPost("attachments.json", json, HTTP_STATUS_CREATED);
		attachment.setHash(hash);
	}

    /**
     * Deletes an attachment.
     *
     * @param id
     * @throws ApiException
     */
	public void deleteAttachment(Long id) throws ApiException {
		String url = "attachments/" + id + ".json";
		doDelete(url, HTTP_STATUS_OK);
	}

    /**
     * Deletes a note thread.
     *
     * @param threadId
     * @throws ApiException
     */
    public void deleteNote(Long threadId) throws ApiException {
        String url = "notes/" + threadId + ".json";
        doDelete(url, HTTP_STATUS_OK);
    }

    /**
     * Finds the first page of workflows associated with the specified mailbox id.
     *
     * @param mailboxId the id of the mailbox
     * @return a Page of Workflow objects
     * @throws ApiException
     */
    public Page getWorkflows(Long mailboxId) throws ApiException {
        return getPage("mailboxes/" + mailboxId + "/workflows.json", Workflow.class, HTTP_STATUS_OK);
    }

    /**
     * Get a page of workflows for the specified mailbox.
     *
     * @param mailboxId
     * @param queryParams
     * @return Page
     * @throws ApiException
     */
    public Page getWorkflows(Long mailboxId, Map<String, String> queryParams) throws ApiException {
        return getPage("mailboxes/" + mailboxId + "/workflows.json", queryParams, Workflow.class, HTTP_STATUS_OK);
    }

    /**
     * Runs the specified manual workflow on the specified ticket.
     *
     * @param id
     * @param ticketId
     * @throws ApiException
     */
    public void runManualWorkflow(Long id, Long ticketId) throws ApiException {
        doPost("workflows/" + id + "/conversations/" + ticketId + ".json", null, HTTP_STATUS_OK);
    }

    /**
     * Runs the specified manual workflow on the specified tickets.
     *
     * @param id
     * @param ticketIds
     * @throws ApiException
     */
    public void runManualWorkflow(Long id, Collection<Long> ticketIds) throws ApiException {
        JsonElement tickets = new Gson().toJsonTree(ticketIds);
        JsonObject obj = new JsonObject();
        obj.add("conversationIds", tickets);
        String json = new Gson().toJson(obj);
        doPost("workflows/" + id + "/conversations.json", json, HTTP_STATUS_OK);
    }

	private void setThreadProperties(ConversationThread thread) {
		AbstractThread theThread = (AbstractThread)thread;

		// Set the type of thread
		if (theThread.getClass().isAssignableFrom(BaseLineItem.class)) {
			thread.setType(ThreadType.LineItem);
		} else if (theThread.getClass().isAssignableFrom(Message.class)) {
			thread.setType(ThreadType.Message);
		} else if (theThread.getClass().isAssignableFrom(Note.class)) {
			thread.setType(ThreadType.Note);
		} else if (theThread.getClass().isAssignableFrom(Customer.class)) {
			thread.setType(ThreadType.Customer);
		} else if (theThread.getClass().isAssignableFrom(ForwardParent.class)) {
			thread.setType(ThreadType.ForwardParent);
		} else if (theThread.getClass().isAssignableFrom(ForwardChild.class)) {
			thread.setType(ThreadType.ForwardChild);
		} else if (theThread.getClass().isAssignableFrom(Chat.class)) {
			thread.setType(ThreadType.Chat);
		}
	}

    private String setParams(String url, Map<String,String> params) {
        if (params != null && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            for (String key : params.keySet()) {
                if (sb.indexOf("?") > 0) {
                    sb.append("&");
                } else {
                    sb.append("?");
                }
                sb.append(key).append("=").append(params.get(key));
            }
            return sb.toString();
        }
        return url;
    }

	private String setFields(String url, List<String> fields) {
		if (fields != null && fields.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(url);
			if (url.indexOf("?") > 0) {
				sb.append("&");
			} else {
				sb.append("?");
			}
			sb.append("fields=");

		    String sep = "";
			for (String field : fields) {
				sb.append(sep).append(field);
				sep = ",";
			}
		    url = sb.toString();
		}
		return url;
	}

    private Map<String,String> getCustomerSearchParams(String email, String firstName, String lastName, Integer page) {
        Map<String,String> params = new HashMap<String,String>();
        if (email != null && email.trim().length() > 0) {
            params.put("email", email.trim().toLowerCase());
        }

        if (firstName != null && firstName.trim().length() > 0) {
            params.put("firstName", firstName.trim());
        }

        if (lastName != null && lastName.trim().length() > 0) {
            params.put("lastName", lastName.trim());
        }

        if (page != null && page > 0) {
            params.put("page", String.valueOf(page));
        }
        return params;
    }

	private void addSearchParams(StringBuilder url, String email, String firstName, String lastName, Integer page) {
		if (email != null && email.trim().length() > 0) {
			if (url.toString().indexOf("?") > 0) {
				url.append("&");
			} else {
				url.append("?");
			}
			url.append("email=").append(email.trim());
		}

		if (firstName != null && firstName.trim().length() > 0) {
			if (url.toString().indexOf("?") > 0) {
				url.append("&");
			} else {
				url.append("?");
			}
			url.append("firstName=").append(firstName.trim());
		}

		if (lastName != null && lastName.trim().length() > 0) {
			if (url.toString().indexOf("?") > 0) {
				url.append("&");
			} else {
				url.append("?");
			}
			url.append("lastName=").append(lastName.trim());
		}

		if (page != null) {
			if (url.toString().indexOf("?") > 0) {
				url.append("&");
			} else {
				url.append("?");
			}
			url.append("page=").append(page);
		}
	}

	private Object getItem(String url, Class<?> clazzType, int expectedCode) throws ApiException {
		String json = doGet(url, expectedCode);
		JsonElement obj  = (new JsonParser()).parse(json);
		JsonElement item = obj.getAsJsonObject().get("item");

		return Parser.getInstance().getObject(item, clazzType);
	}

	private Page getPage(String url, Class<?> clazzType, int expectedCode) throws ApiException {
		return getPage(url, null, clazzType, expectedCode);
	}

    private Page getPage(String url, Map<String,String> params, Class<?> clazzType, int expectedCode) throws ApiException {
        url = setParams(url, params);
        String json = doGet(url, HTTP_STATUS_OK);
        JsonElement obj = (new JsonParser()).parse(json);

        Set<Map.Entry<String, JsonElement>> set = obj.getAsJsonObject().entrySet();

        Page p = new Page();

        for (Map.Entry<String, JsonElement> a : set) {
            String key = a.getKey();
            JsonElement val = a.getValue();

            if (key.equals("page")) {
                p.setPage(val.getAsInt());
                continue;
            }
            if (key.equals("pages")) {
                p.setPages(val.getAsInt());
                continue;
            }
            if (key.equals("count")) {
                p.setCount(val.getAsInt());
                continue;
            }
            if (key.equals("items")) {
                p.setItems(getPageItems(val, clazzType));
            }
        }
        return p;
    }

	private ArrayList<Object> getPageItems(JsonElement elem, Class<?> clazzType) {		
		JsonArray ar = elem.getAsJsonArray();
		ArrayList<Object> col = new ArrayList<Object>(ar.size());
		for(JsonElement e : ar) {
            Object o = Parser.getInstance().getObject(e, clazzType);
			if (o != null) {
				col.add(o);
			}
		}
		return col;
	}

	private Object doPost(String url, String requestBody, int expectedCode) throws ApiException {
		HttpURLConnection conn = null;
		try {
		    conn = getConnection(apiKey, url, METHOD_POST, requestBody != null);

			if (requestBody != null) {
				conn.setDoOutput(true);
				OutputStream output = null;
				try {
					output = conn.getOutputStream();
					output.write(requestBody.getBytes("UTF-8"));
				} finally {
					if (output != null) {
						try { output.close(); } catch (IOException ignored) {}
					}
				}
			}
			conn.connect();
			checkStatusCode(conn, expectedCode);

			if (url.equals("attachments.json")) {
				return getAttachmentHashFromPost(conn);
			} else {
				return getIdFromPost(conn);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			close(conn);
		}
	}

	private Long getIdFromPost(HttpURLConnection conn) {
		String location = conn.getHeaderField("LOCATION");
		if (location != null && location.trim().length() > 0) {
			return new Long(location.substring(location.lastIndexOf("/") + 1, location.lastIndexOf(".")));
		} else {
			return null;
		}
	}

	private String getAttachmentHashFromPost(HttpURLConnection conn) throws RuntimeException {
		String hash = null;
		BufferedReader br = null;
		String response;
		try {
			br = new BufferedReader(new InputStreamReader((getInputStream(conn)), Charset.forName("UTF8")));
			response = getResponse(br);
			JsonElement obj  = (new JsonParser()).parse(response);
			JsonElement item = obj.getAsJsonObject().get("item");
			hash = item.getAsJsonObject().get("hash").getAsString();
		}  catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			close(br);
			close(conn);
		}
		return hash;
	}

	private void doPut(String url, String requestBody, int expectedCode) throws ApiException {
		HttpURLConnection conn = null;
		try {
			conn = getConnection(apiKey, url, METHOD_PUT, requestBody != null);
			if (requestBody != null) {
				conn.setDoOutput(true);
				OutputStream output = null;
				try {
					output = conn.getOutputStream();
					output.write(requestBody.getBytes("UTF-8"));
				} finally {
					if (output != null) {
						try { output.close(); } catch (IOException ignored) {}
					}
				}
			}
			conn.connect();
			checkStatusCode(conn, expectedCode);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			close(conn);
		}
	}

	private String doGet(String url, int expectedCode) throws ApiException {
		HttpURLConnection conn = null;
		BufferedReader br  = null;
		String response    = null;
		try {
			conn = getConnection(apiKey, url, METHOD_GET, false);
			conn.connect();
			checkStatusCode(conn, expectedCode);

			br = new BufferedReader(new InputStreamReader((getInputStream(conn)), Charset.forName("UTF8")));
			response = getResponse(br);
		} catch(Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(br);
			close(conn);
		}
		return response;
	}

	private void doDelete(String url, int expectedCode) throws ApiException {
		HttpURLConnection conn = null;
		try {
			conn = getConnection(apiKey, url, METHOD_DELETE, false);
			conn.connect();
			checkStatusCode(conn, expectedCode);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			close(conn);
		}
	}

	private HttpURLConnection getConnection(String apiKey, String url, String method, boolean hasRequestBody) throws Exception {
		URL aUrl = new URL(BASE_URL + url);

		HttpURLConnection conn = (HttpURLConnection) aUrl.openConnection();

		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod(method);

		if (hasRequestBody) {
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
		}
		conn.setRequestProperty("Authorization", "Basic " + getEncoded(apiKey + ":x"));
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		return conn;
	}

	private void checkStatusCode(HttpURLConnection conn, int expectedCode) throws ApiException, IOException {
		int code = conn.getResponseCode();
		if (code == expectedCode) {
			return;
		}
		switch(code) {
			case 400:
				throw new InvalidFormatException("The request was not formatted correctly");
			case 401:
				throw new InvalidApiKeyException("Invalid API key");
			case 402:
				throw new ApiKeySuspendedException("API key suspended");
			case 403:
				throw new AccessDeniedException("Access denied");
			case 404:
				throw new NotFoundException("Resource not found");
			case 405:
				throw new InvalidMethodException("Invalid method type");
			case 429:
				throw new ThrottleRateException("Throttle limit reached. Too many requests");
			case 500:
				throw new ServerException("Application error or server error");
			case 503:
				throw new ServiceUnavailableException("Service Temporarily Unavailable");
			default:
				throw new ApiException("API key suspended");
		}
	}

	private String getResponse(BufferedReader reader) throws IOException {
		StringBuilder sb = new StringBuilder();

    	String line;
    	while ((line = reader.readLine()) != null) {
    		sb.append(line);
    	}

    	return sb.toString();
	}

	private void close(HttpURLConnection conn) {
		if (conn != null) {
			try {
				conn.disconnect();
			} catch (Exception e) {
				// ignore
			}
		}
	}
	private void close(BufferedReader reader) {
		if (reader != null) {
    		try {
    			reader.close();
			} catch (IOException e) {
				// ignore
			}
    	}
	}

	private InputStream getInputStream(HttpURLConnection conn) throws IOException {
        String encoding = conn.getContentEncoding();

		InputStream inputStream = null;

		//create the appropriate stream wrapper based on
		//the encoding type
		if (encoding != null) {
			if (encoding.equalsIgnoreCase("gzip")) {
				inputStream = new GZIPInputStream(conn.getInputStream());
			} else if (encoding.equalsIgnoreCase("deflate")) {
				inputStream = new InflaterInputStream(conn.getInputStream(), new Inflater(true));
			}
		}
		if (inputStream == null) {
			inputStream = conn.getInputStream();
		}
		return inputStream;
	}

	private String getEncoded(String val) {
		return (new BASE64Encoder()).encode(val.getBytes());
	}

	private String getDecoded(String val) {
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return new String(decoder.decodeBuffer(val));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
