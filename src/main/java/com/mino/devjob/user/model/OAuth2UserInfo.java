package com.mino.devjob.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.user.type.AuthProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class OAuth2UserInfo implements OidcUser, Serializable {
	protected static final long serialVersionUID = -1L;

	@MongoId
	protected ObjectId id;

	protected String name;

	protected String email;

	protected String nickName;

	protected String authProvider;

	protected String image;

	protected Map<String, Object> attributes;

	protected List<Recruit> recruits;

	protected List<Book> books;

	protected OAuth2UserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
		this.authProvider = getAuthProviderEnum().getProviderType();
		setAttribute();
	}

	protected abstract void setAttribute();

	protected abstract AuthProvider getAuthProviderEnum();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + getAuthProvider()));
	}

	@Override
	public Map<String, Object> getClaims() {
		return this.getAttributes();
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return null;
	}

	@Override
	public OidcIdToken getIdToken() {
		return null;
	}

	public void addRecruit(Recruit recruit) {
		if (this.recruits == null) {
			this.recruits = new ArrayList<>();
		}

		this.recruits.add(recruit);
	}

	public void addBook(Book book) {
		if (this.books == null) {
			this.books = new ArrayList<>();
		}

		this.books.add(book);
	}
}
